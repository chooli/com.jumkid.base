package com.jumkid.base.model;

import java.util.ArrayList;

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
 */


public class ApiSpecService extends AbstractCommandService implements
		IApiSpecService {
	
	private ArrayList<ApiSpec> apiSpecs;

	@Override
	public Command execute(Command cmd) throws Exception {
		super.execute(cmd);
		
		try {
    		
	        if (isManager("apimanager")) {
	            if (isAction("load")) {
	            	String module = (String)cmd.getParams().get("module");
	            	
	            	for (ApiSpec apiSpec : apiSpecs) {
	            		if( module.equals(apiSpec.getModule()) &&
	            			apiSpec.getObject()!=null) {
	            			cmd.addResults("apispec", apiSpec); 
	            			break;
	            		}
	            	}
	            	
	            }else
	            if (isAction("list")) {
	            	cmd.setTotoalRecords(apiSpecs.size());
	                
	                cmd.addResults("apispecs", apiSpecs);                
	            }
	            
	        }
		}catch (Exception e) {
            cmd.setError(e.getMessage());   
        } 
        return cmd;
        
	}

	public ArrayList<ApiSpec> getApiSpecs() {
		return apiSpecs;
	}

	public void setApiSpecs(ArrayList<ApiSpec> apiSpecs) {
		this.apiSpecs = apiSpecs;
	}

}
