package com.jumkid.base.view;
/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * Jumkid Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * 
 * Tel: +852 8199 9605
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 * 
 * Modification History
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0b815     Aug2008     chooli      creation
 *       
 */
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;

public class HtmlView extends AbstractView {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String contentType;
	
	/**
     * Renders the view by marshalling the model data (set in the controller)
     * 
     */
    protected void renderMergedOutputModel(Map<String, Object> map, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	    	
    	// this is the business model data (typically a POJO) that was set and returned by the controller
    	Object model = map.get("model");
    	String html = (String)model;    	
    	html = "\n" + html;
    	
    	// write the HTML to the response
    	response.setContentType(getContentType());    
    	response.getOutputStream().write(html.getBytes("UTF-8"));    	
    }

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
