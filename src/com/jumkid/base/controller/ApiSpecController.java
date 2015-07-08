package com.jumkid.base.controller;
/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * 
 * http://www.jumkid.com
 * mailto:support@jumkid.com
 *
 * (c)2013 Jumkid Ltd. All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0        Oct2014      chooli        creation
 * 
 */

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jumkid.base.model.Command;
import com.jumkid.base.model.IApiSpecService;
import com.jumkid.base.model.ServiceSession;

@Controller
public class ApiSpecController {
	
	private IApiSpecService apiSpecService;
	
	@RequestMapping(value="/apispec/list", method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	public String list(HttpServletRequest request){
		HashMap<String, Object> params = new HashMap<String, Object>();
        ServiceSession sSession = new ServiceSession(request);
        try {
        	    Command	cmd = apiSpecService.execute( new Command("apimanager", "list", params) );
            	                	
            	return sSession.wrapCommand(cmd, "apispecs", true).getJsonResult();
        	
        } catch (Exception e) {
        	sSession.setErrors(e.getMessage());
        }
        
        return sSession.toServiceJSONResult();
	}

	public IApiSpecService getApiSpecService() {
		return apiSpecService;
	}

	public void setApiSpecService(IApiSpecService apiSpecService) {
		this.apiSpecService = apiSpecService;
	}

}
 