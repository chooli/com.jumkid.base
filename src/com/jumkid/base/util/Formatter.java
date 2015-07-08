package com.jumkid.base.util;

/* 
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * (c)2013 Jumkid All rights reserved.
 *
 * Modification History
 *
 * VERSION     |     DATE      |   DEVELOPER  |  DESC
 * ----------------------------------------------------------------
 * 1.0           Jan2008         Chooli          Creation
 * 
 */
 
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Formatter {

	//pattern style
    public final static String MM_dd_yyyy = "MM-dd-yyyy";
    public final static String yyyy_MM_dd = "yyyy-MM-dd";
    public final static String MMddyyyy = "MMddyyyy";
    public final static String yyyyMMdd = "yyyyMMdd";
    
    public final static String MM_dd_yyyy_HH_mm_ss = "MM-dd-yyyy HH:mm:ss";
    public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public final static String MMddyyyy_HH_mm_ss = "MMddyyyy HH:mm:ss";
    public final static String yyyyMMdd_HH_mm_ss = "yyyyMMdd HH:mm:ss";
    
    public final static long ONE_DAY_TIME_LONG = 24*60*60*1000;
    
    public final static String TYPE_JSON_NUMBER = "NUMBER";
    public final static String TYPE_JSON_BOOLEAN = "BOOLEAN";
    public final static String TYPE_JSON_STRING = "STRING";
    public final static String TYPE_JSON_OBJECT = "OBJECT";
    public final static String TYPE_JSON_DATETIME = "DATETIME";
    public final static String TYPE_JSON_COLLECTION = "COLLECTION";
    public final static String TYPE_JSON_NULL= "NULL";
    
    // singleton object
    private static Formatter instance;
    
    public static Formatter getInstance() {
        if (instance == null) {
          instance = new Formatter();
        }
        return instance;
    }
    
    public static String toXMLTag(String tag, Object value, boolean isCDATA){
    	if(value==null) return toXMLEmptyTag(tag);
    	return toXMLTag(tag, String.valueOf(value), isCDATA);
    }
    
    public static String toXMLTag(String tag, String value, boolean isCDATA){
    	if(value.isEmpty()) return toXMLEmptyTag(tag);
    	return toXMLStartTag(tag) + (isCDATA ? cdata(value) : value) +	toXMLEndTag(tag);
    }
    
    public static String toXMLEmptyTag(String tag){
    	return "<" + tag + "/>";
    }
    
    public static String toXMLStartTag(String tag){
    	return "<" + tag + ">";
    }
    
    public static String toXMLEndTag(String tag){
    	return "</" + tag + ">";
    }
    
    public static String cdata(Object data) {
        return "<![CDATA[" + (data==null?"":XmlUtil.SanitizeXmlString(data.toString())) + "]]>";
    }
    
    public static String toJSONString(String name, String type, boolean isEnd){
    	if(TYPE_JSON_NULL.equals(type)){
    		return "\""+name+"\":null"+(isEnd?"":",");
    	}	
    	return "";
    }
    
    public static String toJSONString(String name, String type, String value, boolean isEnd){
    	if(TYPE_JSON_STRING.equals(type)){
    		return "\""+name+"\":"+(value==null||value.isEmpty()?null:"\""+JSONObject.escape(value)+"\"")+(isEnd?"":",");
    	}else
    	if(TYPE_JSON_OBJECT.equals(type)){
    		return "\""+name+"\":"+(value==null||value.isEmpty()?null:value)+(isEnd?"":",");
    	}    	
    	return "";
    }
    
    public static String toJSONString(String name, String type, Integer value, boolean isEnd){
    	if(TYPE_JSON_NUMBER.equals(type)){
    		return "\""+name+"\":"+(value==null?null:value)+(isEnd?"":",");
    	}
    	return "";
    }
    
    public static String toJSONString(String name, String type, Float value, boolean isEnd){
    	if(TYPE_JSON_NUMBER.equals(type)){
    		return "\""+name+"\":"+(value==null?null:value)+(isEnd?"":",");
    	}
    	return "";
    }
    
    public static String toJSONString(String name, String type, Long value, boolean isEnd){
    	if(TYPE_JSON_NUMBER.equals(type)){
    		return "\""+name+"\":"+(value==null?null:value)+(isEnd?"":",");
    	}
    	return "";
    }
    
    public static String toJSONString(String name, String type, Boolean value, boolean isEnd){
    	if(TYPE_JSON_BOOLEAN.equals(type)){
    		return "\""+name+"\":"+(value==null?"false":value)+(isEnd?"":",");
    	}
    	return "";
    }
    
    public static String toJSONString(String name, String type, Timestamp value, boolean isEnd){
    	if(TYPE_JSON_DATETIME.equals(type)){
    		return "\""+name+"\":\""+(value==null?"":value.getTime())+(isEnd?"\"":"\",");
    	}
    	return "";
    }
    
    public static String toJSONString(String name, String type, Date value, boolean isEnd){
    	try{
    		if(TYPE_JSON_DATETIME.equals(type)){
        		return "\""+name+"\":\""+(value==null?"":Formatter.dateToString(value, Formatter.yyyy_MM_dd))+(isEnd?"\"":"\",");
        	}
    	}catch(Exception e){
    		//void
    	}
    	
    	return "";
    }
    
	public static String toJSONString(String name, String type, Object[] value, boolean isEnd){
    	StringBuffer sb = new StringBuffer();
    	if(TYPE_JSON_COLLECTION.equals(type) && value!=null){
    		sb.append("\""+name+"\":[");
    		for(int i=0;i<value.length;i++){
    			sb.append("\""+JSONObject.escape(value[i].toString())+(i==value.length-1?"\"":"\","));   			
    		}
    		sb.append(isEnd ? "]" : "],");
    	}else{
    		sb.append("\""+name+"\":null"+(isEnd?"":","));
    	}
    	return sb.toString();
    }
    
    public static String htmlToText(String htmlString){
    	return htmlString.replaceAll("\\<.*?\\>", "").replaceAll("\\s", " ");
    }
    
    public Timestamp stringToTimestamp(String date, String format) throws ParseException {
        if (date==null) return null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        return new Timestamp(df.parse(date).getTime());
    }
    public static String timestampToString(Timestamp ts, String format) throws ParseException {
        if (ts==null) return null;        
        return new SimpleDateFormat(format).format(ts);        
    }
    
    public static Date stringToDate(String date, String format) throws ParseException {
    	if (date==null || date.isEmpty()) return null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(date);
    }
    public static String dateToString(Date date, String format) throws ParseException {
        if (date==null) return null;        
        return new SimpleDateFormat(format).format(date);        
    }
      
    public String maxLength(String s,int length) {
        if (s==null) return s;        
        if (s.length()>length) {
            s = s.substring(0,length-1)+"..";
        }
        return s;
    }
    
    public String stripTags(String input)
    {
       while (input.indexOf ("<") >= 0)
       {
          int beginStrip = input.indexOf ("<");
          int endStrip = input.indexOf (">", beginStrip);
          if (endStrip < 0)
          {
             input = input.substring (0, beginStrip);
          }
          else
          {
             input = input.substring (0, beginStrip) + input.substring
                (endStrip+1);
          }
       }

       return input;
    }
    
    public String capitalize(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
    

    public String twoDigits(int value) {
        String out = new Integer(value).toString();
        if (out.length()<=1) out = "0"+out;
        return out;
    }

    public String twoDigits(String out) {        
        if (out.length()<=1) out = "0"+out;
        return out;
    }        
    
    public boolean isValidEmailAddress(String email) {
	   boolean result = true;
	   try {
	      InternetAddress emailAddr = new InternetAddress(email);
	      emailAddr.validate();
	   } catch (AddressException ex) {
	      result = false;
	   }
	   return result;
	}
    
    
}





