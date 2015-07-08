package com.jumkid.base.model;

/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * 
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2014 Jumkid Innovation. All rights reserved.
 *
 *
 */

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jumkid.base.util.Formatter;

public class AbstractCommandService {

	protected Command cmd;
	
	protected String site;
		
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	/**
     * Executes the commands and returns it back
     * 
     * @param cmd is the command bean
     * @return command which is same as input param plus result values
     */
	public Command execute(Command cmd) throws Exception{
		this.cmd = cmd;
		this.site = (String)cmd.getParams().get("site");
		
		return cmd;
	}
	
	public <T> CommonBean transformRequestToBean(HttpServletRequest request, Class<T> objectClass, ICommonBeanDao beanDao){
		Integer id = request.getParameter("id")!=null&&!"".equals(request.getParameter("id"))?Integer.valueOf(request.getParameter("id")):null;
		CommonBean bean;
		
		try {
			
			if (id!=null) {
	        	bean = beanDao.load(id);
	        	
	        } else {
	    		bean = (CommonBean)objectClass.newInstance();
	    		bean.setCreatedOn( new Timestamp(Calendar.getInstance().getTimeInMillis()) );
	        	bean.setCreatedBy(getLoginUser());
	        }    
			
	        //parse request by fields
	        bean = this.fillInValueByRequest(bean, request);
	        
			String _modifiedOn = request.getParameter("modifiedOn"); 
	        if (_modifiedOn!=null) {
	        	bean.setModifiedOn( "".equals(request.getParameter("modifiedOn"))?null:new Timestamp(Long.valueOf( request.getParameter("modifiedOn")).longValue() ) );
	        }else {
	        	bean.setModifiedOn( new Timestamp(Calendar.getInstance().getTimeInMillis()) );
	        }	
	        bean.setModifiedBy(getLoginUser());
			
	        return bean;
	        
		} catch (Exception e){
        	e.printStackTrace();
        }
        
		return null;
	}
	
	/**
	 * check command manager
	 * 
	 * @param manager
	 * @return
	 */
	protected boolean isManager(String manager){
		if(manager.equals(cmd.getManager()) || manager.toLowerCase().equals(cmd.getManager())
				|| manager.toUpperCase().equals(cmd.getManager())) return true;
		return false;
	}
	
	/**
	 * check command action
	 * 
	 * @param action
	 * @return
	 */
	protected boolean isAction(String action){
		if(action.equals(cmd.getAction()) || action.toLowerCase().equals(cmd.getAction())
				|| action.toUpperCase().equals(cmd.getAction())) return true;
		return false;
	}
	
	protected CommonBean fillInValueByRequest(CommonBean bean, HttpServletRequest request){

		try{
			Field[] myFields = bean.getClass().getDeclaredFields();
			Field[] parentFields = bean.getClass().getSuperclass().getDeclaredFields();
			Field[] fields = bean.combine(myFields, parentFields);
			int flength = fields.length;
			outerLoop:
	        for (int i=0;i<flength;i++) {
	        	
	        	java.lang.reflect.Field field = fields[i];
	        	field.setAccessible(true);
	        	
	        	//check annotation
	        	Annotation[] annotations = field.getDeclaredAnnotations();
	        	for(Annotation annotation : annotations){
	        	    if(annotation instanceof javax.persistence.Transient){
	        	        continue outerLoop;
	        	    }
	        	}
	        	
	            
	            if(!java.lang.reflect.Modifier.isStatic(field.getModifiers())){
	                
	            	Method method = null;
					String methodName = "set" + Formatter.getInstance().capitalize(field.getName());
					Object reqValue = request.getParameter(field.getName());
					if (field.getType().isArray()) { //get values for array parameter
						reqValue = request.getParameterValues(field.getName()+"[]");
						if(reqValue==null){
							@SuppressWarnings("rawtypes")
							Class componentType = field.getType().getComponentType();
							reqValue = Array.newInstance(componentType, 0);
						}
					}else
					if(reqValue!=null){
						String _reqValue = reqValue.toString();
						if(String.class.equals(field.getType())){
							reqValue = _reqValue;
						}
						if(Boolean.class.equals(field.getType())){
							reqValue = _reqValue.isEmpty() ? Boolean.FALSE : Boolean.parseBoolean(_reqValue);
						}else
						if(Integer.class.equals(field.getType())){
							reqValue = _reqValue.isEmpty() ? null : Integer.parseInt(_reqValue);
						}else
						if(Long.class.equals(field.getType())){
							reqValue = _reqValue.isEmpty() ? null : Long.parseLong(_reqValue);
						}else
						if(Float.class.equals(field.getType())){
							reqValue = _reqValue.isEmpty() ? null : Float.parseFloat(_reqValue);
						}else
						if(Double.class.equals(field.getType())){
							reqValue = _reqValue.isEmpty() ? null : Double.parseDouble(_reqValue);
						}else
						if(Date.class.equals(field.getType())){
							reqValue = _reqValue.isEmpty() ? null : Formatter.stringToDate(_reqValue, Formatter.yyyy_MM_dd);
						}else
						if(Timestamp.class.equals(field.getType())){
							reqValue = _reqValue.isEmpty() ? null : new Timestamp(Long.parseLong(_reqValue));
						}	
						
					}
					
					try{
						method = bean.getClass().getMethod(methodName, new Class[]{field.getType().isArray()?String[].class:field.getType()});
					}catch(NoSuchMethodException nse){
						logger.debug("bean method " + methodName + " is not accessible");
						continue;
					}
					
					if(method!=null && reqValue!=null){
						method.invoke(bean, reqValue);
					}
	                    
	            }
	            
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
        
        return bean;
	}
	
	protected CommonBean fillInConcurrencyInfo(CommonBean bean, HttpServletRequest request){
		if(bean==null) return null;
		
		String _modifiedOn = request.getParameter("modifiedOn"); 
        if (_modifiedOn!=null) {
        	bean.setModifiedOn( "".equals(request.getParameter("modifiedOn"))?null:new Timestamp(Long.valueOf( request.getParameter("modifiedOn")).longValue() ) );
        }else {
        	bean.setModifiedOn( new Timestamp(Calendar.getInstance().getTimeInMillis()) );
        }	
        bean.setModifiedBy(ServiceSession.getUsername(request));   
        
        return bean;
	}
	
	private String getLoginUser(){
		try{
			org.springframework.security.core.userdetails.User _user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	if(_user!=null)
	    		return _user.getUsername();
		}catch(Exception e){
			//void
		}
    	
    	return null;
    }

	public Command getCmd() {
		return cmd;
	}

	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
	
}
