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
 * http://www.clientfills.com
 * mailto:info@clientfills.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 * 
 * Modification History
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0         march2008   mathias      creation
 * 1.0         march2008   chooli       add <response> to output      
 */

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;

import com.jumkid.base.model.ServiceSession;

/*
 * An XML view resolver.  This view is invoked if view returned by controller
 * matches this view's bean name in the configuration file views.xml.
 */
public class JsonView extends AbstractView {
    
	/**
	 * Apache commons-logging.jar logger - uncomment to enable
	 */
    //protected final Log logger = LogFactory.getLog(getClass());
    
    /**
     * Renders the view by marshalling the model data (set in the controller)
     * into XML and writing the XML to the response output stream.
     */
    protected void renderMergedOutputModel(Map<String, Object> map, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	    	
    	// this is the business model data (typically a POJO) that was set and returned by the controller
    	Object model = map.get("model");
    	ServiceSession sSession = (ServiceSession)model;    
    	String json = "";
    	if (sSession!=null) {
    		json = (sSession.getJsonResult()==null ? sSession.toServiceJSONResult() : sSession.getJsonResult());
    	}
    	
    	// write the JSON data to the response
    	response.setContentType("text/plain; charset=UTF-8");
    	response.getOutputStream().write(json.getBytes("UTF-8"));    	
    }
    
}

