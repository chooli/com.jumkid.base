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

public class NoUserLoginException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2632048825707735903L;
	
	/**
	 * 
	 * @param errorMessage
	 */
	public NoUserLoginException(String errorMessage){
		super(errorMessage);		
	}
	
	/**
	 * 
	 * @param errorMessage
	 * @param e
	 */
	public NoUserLoginException(String errorMessage, Throwable e){
		super(errorMessage, e);		
	}
	
}
