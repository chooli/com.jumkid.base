package com.jumkid.base.exception;

public class SystemServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1205798541086813244L;

	/**
	 * 
	 * @param errorMessage
	 */
	public SystemServiceException(String errorMessage){
		super(errorMessage);		
	}		
	
	/**
	 * 
	 * @param errorMessage
	 * @param e
	 */
	public SystemServiceException(String errorMessage, Throwable e){
		super(errorMessage, e);		
	}
}
