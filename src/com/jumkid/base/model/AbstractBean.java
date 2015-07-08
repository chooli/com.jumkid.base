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
 * 1.0        Jan2013      chooli      creation
 * 
 *
 */

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

import com.jumkid.base.util.Formatter;


public abstract class AbstractBean implements java.io.Serializable, Cloneable  {   
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract String[] getPropertyNames();
	
	/**
	 * 
	 * @return
	 */
	public abstract String toXML();
    
    /**
     * toJSON method using reflection
     * 
     * @param locale
     * @return Object as XML
     */
    public abstract String toJSON();
    
    
    /**
     * 
     * @return
     */
    public abstract String toCSV();
        
    /**
     * 
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
	protected String toJSONString(Field field, Object value, boolean islast) throws Exception{
    	String json = "";
    	
		if( field.getType().isAssignableFrom(String.class) ){
        	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_STRING, 
        			(String)value, islast);
        }else
        if( field.getType().isAssignableFrom(Integer.class)){
        	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_NUMBER, 
        			(Integer)value, islast);
        }else
        if( field.getType().isAssignableFrom(Long.class)){
         	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_NUMBER, 
         			(Long)value, islast);
        }else
        if( field.getType().isAssignableFrom(Float.class)){
          	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_NUMBER, 
          			(Float)value, islast);
        }else
        if( field.getType().isAssignableFrom(Boolean.class)){
        	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_BOOLEAN, 
        			(Boolean)value, islast);
        }else
        if( field.getType().isAssignableFrom(Date.class) ){
        	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_DATETIME, 
        			(Date)value, islast);
        }else
        if( field.getType().isAssignableFrom(Timestamp.class) ){
        	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_DATETIME, 
        			(Timestamp)value, islast);
        }else
        if( field.getType().isArray() ){
        	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_COLLECTION, 
        			(Object[])value, islast);
        }
		
    	return json;
    }
    
    /**
     * Output a plain text for search purpose
     * 
     * @return
     */
    public String toPlainText(){
		String text = "";
		
		try{
			for(String propertyName : getPropertyNames()){
				Object value = this.getClass().getMethod("get" + Formatter.getInstance().capitalize(propertyName)).invoke(this);
				
				if(value!=null) text += String.valueOf(value) + " ";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
				
		return text.replaceAll("\\<[^>]*>","").replaceAll("\\r|\\n|\\t","");
	}
    
    
	public Object clone() {
        try {
        	return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }


}
