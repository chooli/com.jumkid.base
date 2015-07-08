package com.jumkid.base.model;

/* 
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 * (c)2013 Jumkid All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0        Jan2013      chooli      creation
 * 
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;

import com.jumkid.base.model.user.User;
import com.jumkid.base.util.Constants;
import com.jumkid.base.util.Formatter;

public class ServiceSession {
	
	protected static final Log logger = LogFactory.getLog(ServiceSession.class); 
	
	/**
	 * session variables
	 */
	public static final String USER_SESSION_KEY = "sessionUser";
	public static final String SESSION_USER_NAME = "sessionUserName";
	public static final String LOCALE_SESSION_KEY = "sessionLocale";
	public static final String LOCALE_STRING_SESSION_KEY = "sessionLocaleString";
	public static final String MODULE_PERMISSIONS_SESSION_KEY = "sessionModulePermissions";
	
	public static final String SESSION_REQUEST_ERROR = "requestError";
	
	public static final String FORMAT_XML = "XML";
	public static final String FORMAT_JSON = "JSON";
	
	private HttpServletRequest request;
	private Long totalRecords;
	private Integer totalPages;
	private Integer currentPage;
	private Integer nextPage;
	private Integer previousPage;
    private Object[] facetFields;
	
	private String errorMessage;
    
    private String jsonResult;
    private String xmlResult;
    
    private boolean success = false;
    
    public ServiceSession(HttpServletRequest request){
    	this.request = request;
    }
    
    public ServiceSession(boolean _success){
		this.setSuccess(_success);
	}
    
    /**
     * 
     * @param cmd
     * @param successView
     */
    public ServiceSession wrapCommand(Command cmd){
    	
    	this.setErrors(cmd.getError());   
    	if("xmlView".equals(this.getSuccessView())){
        	this.toServiceXMLResult();
        }else{
        	this.toServiceJSONResult();
        }
    	
    	return this;
	}
    
    /**
     * Wrap a command object by determine its multiple object or single object
     * 
     * @param cmd
     * @param successView
     * @param beanName
     * @param multiple
     */
    @SuppressWarnings("unchecked")
	public ServiceSession wrapCommand(Command cmd, String beanName, boolean multiple){
    	
    	if(multiple){
			List<AbstractBean> beans = (List<AbstractBean>)cmd.getResults().get(beanName);
    		this.wrapCommand(cmd, this.getSuccessView(), beans, beanName);
    	}else{
    		AbstractBean bean = (AbstractBean)cmd.getResults().get(beanName);
    		this.wrapCommand(cmd, this.getSuccessView(), bean);
    	}
    	
    	return this;
    }
    
    /**
     * 
     * @param cmd
     * @param successView
     * @param bean
     */
    private ServiceSession wrapCommand(Command cmd,
			String successView, AbstractBean bean){
    	this.setErrors(cmd.getError());
    	
    	if(bean!=null){
    		this.setTotalRecords(new Long(1));
    	}
    	
    	if("xmlView".equals(successView)){
        	this.toServiceXMLResult(bean);
        }else{
        	this.toServiceJSONResult(bean);
        }
    	
    	return this;
	}
    
    /**
     * 
     * @param cmd
     * @param successView
     * @param beans
     * @param beanName
     */
    private ServiceSession wrapCommand(Command cmd,
			String successView, List<AbstractBean> beans, String beanName){
    	
    	this.setErrors(cmd.getError());
    	//this.setTotalRecords( new Long(cmd.getTotoalRecords()) );
    	if("xmlView".equals(successView)){
        	this.toServiceXMLResult(beans, beanName);
        }else{
        	this.toServiceJSONResult(beans, beanName);
        }
    	return this;
	}
    
    /**
     * 
     * @param cmd
     * @param beanName
     * @param value
     * @param format
     */
    public ServiceSession wrapCommand(Command cmd,
			String beanName, String format){
    	this.setErrors(cmd.getError());
    	String value = (String)cmd.getResults().get(beanName);
    	if(FORMAT_XML.equals(format)){
        	this.toServiceXMLResult(value);
        }else{
        	this.toServiceJSONResult(beanName, value);
        }
    	
    	return this;
    }
    
    /**
     * wrap page information to service session
     * 
     * @param page
     * @param start
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> void wrapPage(Page<T> page, Integer start){
    	this.setTotalRecords(page.getTotalElements());
    	this.setTotalPages(page.getTotalPages());
    	this.setCurrentPage(start);
    	if (page.hasNext()) {
    		Integer nextPage = page.nextPageable().getPageNumber();
    		this.setNextPage(nextPage);
    	}
    	if (page.hasPrevious()) {
    		Integer previousPage = page.previousPageable().getPageNumber();
    		this.setPreviousPage(previousPage);
    	}
    	
    	if(page instanceof FacetPage){
    		List<SimpleFacetFieldEntry> entries = ((FacetPage) page).getFacetResultPage("vlname").getContent();
    		ArrayList<String> lst = new ArrayList<String>();
    		String json = null;
    		for(SimpleFacetFieldEntry entry : entries){
    			json = "{";
    			json += Formatter.toJSONString(entry.getField().getName(), Formatter.TYPE_JSON_STRING, 
    					entry.getValue(), false);
    			json += Formatter.toJSONString("count", Formatter.TYPE_JSON_NUMBER, 
    					entry.getValueCount(), true);
    			json += "}";
    			lst.add(json);
    		}
    		this.setFacetFields(lst.toArray());
    	}
    }
    
	public HttpServletRequest getHttpRequest() {
		return request;
	}
	public void setHttpRequest(HttpServletRequest request) {
		this.request = request;
	}	
	public String getJsonResult() {
		return jsonResult;
	}
	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}
	
	public String getSuccessView(){
		if(this.request!=null) return (String)this.request.getAttribute("successView");
		return null;
	}
	public static String getSuccessView(HttpServletRequest request){
		return (String)request.getAttribute("successView");
	}
	
	/**
	 * Gets the User from the session
	 * 
	 * @param request The current request
	 * @return The User object
	 */
	public User getUser(){
		return (User)this.request.getAttribute(USER_SESSION_KEY);
	}
	public static User getUser(HttpServletRequest request) {		
		return (User)request.getAttribute(USER_SESSION_KEY);
	}
	public String getUsername(){
		return (String)this.request.getAttribute(SESSION_USER_NAME);
	}
	public static String getUsername(HttpServletRequest request){
		return (String)request.getAttribute(SESSION_USER_NAME);
	}
    
    public static String getLocale(HttpServletRequest request) {
        return getLocale(request.getSession());
    }
    public static String getLocale(HttpSession session) {
    	if(session.getAttribute(LOCALE_STRING_SESSION_KEY)!=null) return (String)session.getAttribute(LOCALE_STRING_SESSION_KEY);
    	else return Constants.DEFAULT_LOCALE.toString();        
    }

	public static void setLocale(HttpServletRequest request, Locale locale) {
        setLocale(request.getSession(),locale);
    }
    public static void setLocale(HttpSession session, Locale locale) {
        try {
            session.setAttribute(LOCALE_SESSION_KEY, locale);
            session.setAttribute(LOCALE_STRING_SESSION_KEY, locale.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getStackTrace()[0]);
            logger.error(e.getStackTrace()[1]);
        }
    }
    
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	/**
	 * Sets the user attribute to the session
	 * 
	 * @param request The current request
	 * @param user The user to set. Cannot be null
	 */
    public void  setUser(Object user) {
		this.request.setAttribute(USER_SESSION_KEY, user);
	}
	public static void setUser(HttpServletRequest request, Object user) {
		request.setAttribute(USER_SESSION_KEY, user);
	}	
    public static void setUser(HttpSession session, Object user) {
       session.setAttribute(USER_SESSION_KEY, user);
    }
    public void setUsername(String username){
    	this.request.setAttribute(SESSION_USER_NAME, username);
    }
    public static void setUsername(HttpServletRequest request, String username){
    	request.setAttribute(SESSION_USER_NAME, username);
    }
    
	/**
	 * Indicates whether a user is logged in
	 * 
	 * @param request The request
	 * @return T/F
	 */
    public boolean isUserLoggedIn(){
    	return isUserLoggedIn(this.request);
    }
	public static boolean isUserLoggedIn(HttpServletRequest request) {
		return isUserLoggedIn(request.getSession());
	}	
    public static boolean isUserLoggedIn(HttpSession session) {
        User user = (User)session.getAttribute(USER_SESSION_KEY);
        if (user!=null) logger.info("isUserLoggedIn: " + user.getUsername());       
        return  user!= null && user.getId()!=null;
    }
    
    /**
     * To plain json result with success flag and error messages 
     * 
     * @return
     */
    public String toServiceJSONResult(){
    	this.jsonResult = "{";
    	
    	this.jsonResult += Formatter.toJSONString("success", Formatter.TYPE_JSON_BOOLEAN, 
        		(this.getErrors()==null || this.getErrors().isEmpty()), false);    	
    	
    	this.jsonResult += Formatter.toJSONString("totalRecords", Formatter.TYPE_JSON_NUMBER, 
    			(totalRecords!=null?totalRecords:0), false);
    	
    	this.jsonResult += Formatter.toJSONString("error", Formatter.TYPE_JSON_STRING, 
    			this.getErrors(), true);
        
    	this.jsonResult += "}";
    	
		return this.jsonResult;
    }
    
    /**
     * Transform name and value into service json result
     * 
     * @param name
     * @param value
     * @return
     */
    public String toServiceJSONResult(String value, String name){
    	this.jsonResult = "{";
    	
    	this.jsonResult += Formatter.toJSONString("success", Formatter.TYPE_JSON_BOOLEAN, 
        		(this.getErrors()==null || this.getErrors().isEmpty()), false); 
    	
    	this.jsonResult += Formatter.toJSONString("totalRecords", Formatter.TYPE_JSON_NUMBER, 
    			(totalRecords!=null?totalRecords:0), false);
    	
    	this.jsonResult += Formatter.toJSONString(name, Formatter.TYPE_JSON_STRING, value, false);
    	
    	this.jsonResult += Formatter.toJSONString("error", Formatter.TYPE_JSON_STRING, 
    			this.getErrors(), true);
        
    	this.jsonResult += "}";
    	
		return this.jsonResult;
    }
    
    /**
     * Transform name and value into service json result
     * 
     * @param name
     * @param value
     * @return
     */
    public String toServiceJSONResult(Boolean value, String name){
    	this.jsonResult = "{";
    	
    	this.jsonResult += Formatter.toJSONString("success", Formatter.TYPE_JSON_BOOLEAN, 
        		(this.getErrors()==null || this.getErrors().isEmpty()), false); 
    	
    	this.jsonResult += Formatter.toJSONString("totalRecords", Formatter.TYPE_JSON_NUMBER, 
    			(totalRecords!=null?totalRecords:0), false);
    	
    	this.jsonResult += Formatter.toJSONString(name, Formatter.TYPE_JSON_BOOLEAN, value, false);
    	
    	this.jsonResult += Formatter.toJSONString("error", Formatter.TYPE_JSON_STRING, 
    			this.getErrors(), true);
        
    	this.jsonResult += "}";
    	
		return this.jsonResult;
    }
    
    /**
     * Transform single abstractbean into service json result
     * 
     * @param bean
     * @param beanName
     * @return
     */
    public String toServiceJSONResult(AbstractBean bean){
    	this.jsonResult = "{";
    	
    	this.jsonResult += Formatter.toJSONString("success", Formatter.TYPE_JSON_BOOLEAN, 
        		(this.getErrors()==null || this.getErrors().isEmpty()), false);    	
    	
    	if(bean!=null){
    		this.jsonResult += Formatter.toJSONString(bean.getClass().getSimpleName().toLowerCase(), Formatter.TYPE_JSON_OBJECT, 
            		bean.toJSON(), false); 
    	}
        
    	this.jsonResult += Formatter.toJSONString("error", Formatter.TYPE_JSON_STRING, 
    			this.getErrors(), true);
        
    	this.jsonResult += "}";
    	
		return this.jsonResult;
    }
    
    /**
     * Transform list of abstractbeans into service json result
     * 
     * @param beanList
     * @param beanName
     * @return
     */
    public String toServiceJSONResult(List<AbstractBean> beanList, String beanName){
    	return this.toServiceJSONResult(beanList, beanName, this.getTotalRecords());
    }
    
    /**
     * Transform list of abstractbeans into service json result
     * 
     * @param beanList
     * @param beanName
     * @return
     */
    public String toServiceJSONResult(List<AbstractBean> beanList, String beanName, Long total){
    	this.jsonResult = "{";
    	
    	this.jsonResult += Formatter.toJSONString("success", Formatter.TYPE_JSON_BOOLEAN, 
        		(this.getErrors()==null || this.getErrors().isEmpty()), false);    
    	
    	this.jsonResult += Formatter.toJSONString("totalRecords", Formatter.TYPE_JSON_NUMBER, 
    			(total==null?beanList.size():total), false);
    	
    	this.jsonResult += Formatter.toJSONString("totalPages", Formatter.TYPE_JSON_NUMBER, 
    			this.totalPages, false);
    	
    	this.jsonResult += Formatter.toJSONString("currentPage", Formatter.TYPE_JSON_NUMBER, 
    			this.currentPage, false);
    	
    	this.jsonResult += Formatter.toJSONString("nextPage", Formatter.TYPE_JSON_NUMBER, 
    			this.nextPage, false);
    	
    	this.jsonResult += Formatter.toJSONString("previousPage", Formatter.TYPE_JSON_NUMBER, 
    			this.previousPage, false);
    	
    	if(this.facetFields!=null){
    		this.jsonResult += "\"facetFields\":[";
    		for(int i=0;i<this.facetFields.length;i++){
        		this.jsonResult += this.facetFields[i] + (i==this.facetFields.length-1?"":",");
        	} 
    		this.jsonResult += "],";
    	}
    	
    	if(beanList!=null){
    		this.jsonResult += "\""+beanName.toLowerCase()+"\":[";
        	for(int i=0;i<beanList.size();i++){
        		if(beanList.get(i) instanceof AbstractBean) this.jsonResult += beanList.get(i).toJSON() + (i==beanList.size()-1?"":",");
        	}        	
        	this.jsonResult += "],";
    	}    	
        
    	this.jsonResult += Formatter.toJSONString("error", Formatter.TYPE_JSON_STRING, 
    			this.getErrors(), true);
        
    	this.jsonResult += "}";
    	
		return this.jsonResult;
    }
    
    public String toServiceXMLResult(){
    	return this.toServiceXMLResult("");
    }
    
    /**
     * 
     * @param xmlResult
     * @return
     */
    public String toServiceXMLResult(AbstractBean bean){
    	return toServiceXMLResult(bean.toXML());
    }
    
    /**
     * 
     * @param xmlResult
     * @return
     */
    public String toServiceXMLResult(String xml){
    	this.xmlResult = Formatter.toXMLStartTag("response");
    	
    	this.xmlResult += Formatter.toXMLTag("success", 
    			(this.getErrors()==null || this.getErrors().isEmpty())?"true":"false", 
    					false); 
    	
    	this.xmlResult += Formatter.toXMLTag("results", (totalRecords!=null)?totalRecords.toString():"0", false);
    	
    	if(xml!=null && !xml.isEmpty()) this.xmlResult += xml;
    	
    	this.xmlResult += Formatter.toXMLTag("error", (this.getErrors()==null || this.getErrors().isEmpty())?"N/A":this.getErrors(), false); 
    	
    	this.xmlResult += Formatter.toXMLEndTag("response");
    	
		return this.xmlResult;
    }

    /**
     * 
     * @param beanList
     * @param groupName
     * @return
     */
    public String toServiceXMLResult(List<AbstractBean> beanList, String groupName) {
    	this.xmlResult = Formatter.toXMLStartTag("response");
    	
    	this.xmlResult += Formatter.toXMLTag("success", 
    			(this.getErrors()==null || this.getErrors().isEmpty())?"true":"false", 
    					false);
    	
    	this.xmlResult += Formatter.toXMLTag("results", ((totalRecords!=null)?totalRecords.toString():String.valueOf(beanList.size())), false);
    	
    	this.xmlResult += Formatter.toXMLStartTag(groupName.toLowerCase());
    	if(beanList!=null){    		
        	for(AbstractBean bean:beanList){
        		this.xmlResult += bean.toXML();
        	}        	
    	}  
    	this.xmlResult += Formatter.toXMLEndTag(groupName.toLowerCase());
    	
    	this.xmlResult += Formatter.toXMLTag("error", (this.getErrors()==null || this.getErrors().isEmpty())?"N/A":this.getErrors(), false);
    	
    	this.xmlResult += Formatter.toXMLEndTag("response");
    	
		return this.xmlResult;
    }
    
	public String getErrors() {
		if(errorMessage==null || errorMessage.isEmpty()){
			errorMessage = ( this.request==null ? null : (String)this.request.getAttribute(SESSION_REQUEST_ERROR) );
		}
		return errorMessage;
	}

	public void setErrors(String errorMessage) {
		this.errorMessage = errorMessage;
		if(this.request!=null) this.request.setAttribute(SESSION_REQUEST_ERROR, errorMessage);
	}

	public String getXmlResult() {
		return xmlResult;
	}

	public void setXmlResult(String xmlResult) {
		this.xmlResult = xmlResult;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	public Integer getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(Integer previousPage) {
		this.previousPage = previousPage;
	}

	public Object[] getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(Object[] facetFields) {
		this.facetFields = facetFields;
	}
	
}
