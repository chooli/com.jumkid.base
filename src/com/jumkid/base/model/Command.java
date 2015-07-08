package com.jumkid.base.model;

/* 
 * This software is written by Jumkid. and subject
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
 */

import java.util.HashMap;

public class Command {

	private String manager;
    private String action;
    private HashMap<String, Object> params;
    private HashMap<String, Object> results;
    private Integer totoalRecords;
    private String error;
    private Integer userId;
    
    public Command(String error) {
    	this.error = error;
    }
    
    public Command(String manager, String action, HashMap<String, Object> params) {
    	this.manager = manager;
        this.action = action;
        this.params = params;
    } 
    
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public HashMap<String, Object> getParams() {
    	if(this.params==null){
    		this.params = new HashMap<String, Object>();
    	}
        return params;
    }
    
    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
  
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	public HashMap<String, Object> getResults() {
		if(this.results==null){
			this.results = new HashMap<String, Object>();
		}
		return results;
	}

	public void setResults(HashMap<String, Object> results) {
		this.results = results;
	}
	
	public void addResults(String key, Object value){
		if(this.results==null){
			this.results = new HashMap<String, Object>();
		}
		this.results.put(key, value);
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Integer getTotoalRecords() {
		return totoalRecords;
	}

	public void setTotoalRecords(Integer totoalRecords) {
		this.totoalRecords = totoalRecords;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
    

}
