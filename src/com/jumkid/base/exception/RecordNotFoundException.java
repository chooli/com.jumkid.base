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

public class RecordNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8182556660118616148L;

	/**
	 * 
	 * @param errorMessage
	 */
	public RecordNotFoundException(String errorMessage){
		super(errorMessage);		
	}
	
	/**
	 * 
	 * @param errorMessage
	 * @param e
	 */
	public RecordNotFoundException(String errorMessage, Throwable e){
		super(errorMessage, e);		
	}
}
