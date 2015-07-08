package com.jumkid.base.exception;

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
 * 3.0        Jan2013      chooli      creation
 * 
 *
 */

public class BeanValidateException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3627998013030306500L;
	
	private String validationMessage;

	/**
	 * 
	 * @param errorMessage
	 */
	public BeanValidateException(String errorMessage){
		super(errorMessage);	
		this.validationMessage = errorMessage;
	}
	
	/**
	 * 
	 * @param errorMessage
	 * @param e
	 */
	public BeanValidateException(String errorMessage, Throwable e){
		super(errorMessage, e);		
		this.validationMessage = errorMessage;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
}

