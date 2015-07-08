package com.jumkid.base.model;

/* 
 * This software is written by Huluboy and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Huluboy unless differing
 * arrangements between Huluboy and its customer apply.
 *
 *
 * (c)2013 Huluboy All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0        Jan2013      chooli      creation
 * 
 *
 */

import javax.servlet.http.HttpServletRequest;

import com.jumkid.base.exception.BeanValidateException;

public interface AbstractBeanValidatable {

	/**
	 * 
	 * @param request
	 * @return
	 * @throws BeanValidateException
	 */
	public ServiceSession validate(HttpServletRequest request);
	
	/**
	 * 
	 * @param vtype
	 * @param fieldName
	 * @param value
	 * @throws BeanValidateException
	 */
	public void validate(String vtype, String fieldName, Object value) throws BeanValidateException;
	
}
