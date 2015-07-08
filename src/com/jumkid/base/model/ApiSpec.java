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
 * mailto:support@jumkid.com
 * 
 * (c)2013 Jumkid Ltd. All rights reserved.
 * 
 */

public class ApiSpec extends CommonBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -811521875410685218L;
	
	private String module;
	private String url;
	private String format;
	private String action;
	private String input;
	private String output;
	private CommonBean object;
	
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public CommonBean getObject() {
		return object;
	}
	public void setObject(CommonBean object) {
		this.object = object;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	

}
