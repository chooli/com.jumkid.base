package com.jumkid.base.view;
/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 * 
 * Modification History
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 2.0         Dec2009     chooli      creation
 *       
 */
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;

public class XhtmlView extends AbstractView {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String XHML_DOCTYPE_STRING = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n"; 
    
	private String contentType;
	
	/**
     * Renders the view by marshalling the model data (set in the controller)
     * 
     */
    protected void renderMergedOutputModel(Map<String, Object> map, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	    	
    	// this is the business model data (typically a POJO) that was set and returned by the controller
    	Object model = map.get("model");
    	String xhtml = (String)model;    	
    	if (xhtml==null) xhtml="";
    	xhtml = XHML_DOCTYPE_STRING + xhtml;
    	
    	// write the HTML to the response
    	response.setContentType(getContentType());    
    	response.getOutputStream().write(xhtml.getBytes("UTF-8"));    	
    }

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
