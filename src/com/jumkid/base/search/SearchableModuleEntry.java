package com.jumkid.base.search;
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
 * VERSION   |   DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0         July2013       chooli       creation
 * 
 *
 */
import java.util.ArrayList;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

import com.jumkid.base.model.AbstractBean;
import com.jumkid.base.util.Formatter;

public class SearchableModuleEntry extends AbstractBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3318729945418887971L;
	
	@Id
	@Field
	public String id;
	
	@Field("internal_id")
	private String internalId;
	
	@Field("internal_user_id")
	private String internalUserId;
	
	@Field
	private String title;
	
	@Field
	private String module;
	
	@Field
	private String site;
	
	@Field("modified_on")
	private Date modifiedOn;
	
	@Field("label")
	private ArrayList<String> labels;
	
	@Field
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getInternalUserId() {
		return internalUserId;
	}

	public void setInternalUserId(String internalUserId) {
		this.internalUserId = internalUserId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public ArrayList<String> getLabels() {
		return labels;
	}

	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String[] getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * toJSON method using reflection
     * 
     * @param locale
     * @return Object as XML
     */
    public String toJSON(){
    	String json = "{";
        
    	json += Formatter.toJSONString("id", Formatter.TYPE_JSON_NUMBER, 
    											this.getId(), false);
    	
    	try {
    		java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
    		int flength = fields.length;
            for (int i=0;i<flength;i++) {
            	
            	java.lang.reflect.Field field = fields[i];
                
                if(!java.lang.reflect.Modifier.isStatic(field.getModifiers())){
                	Object value = this.getClass().getMethod("get" + Formatter.getInstance().capitalize(field.getName())).invoke(this);
                    
                	json += toJSONString(field, value, (i+1==flength?true:false));
                        
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        json += "}";        
        
        return json;
    }   

	@Override
	public String toCSV() {
		// TODO Auto-generated method stub
		return null;
	}

}
