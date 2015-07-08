package com.jumkid.base.model.user;
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
 * 1.0        Jan2013      chooli      creation
 * 
 *
 */

import javax.servlet.http.HttpServletRequest;

import com.jumkid.base.model.usergroup.UserGroup;
import com.jumkid.base.exception.BeanValidateException;
import com.jumkid.base.model.Command;
import com.jumkid.base.model.ServiceSession;

public interface IUserService {
	
	/**
     * 
     * @param cmd
     * @return
     * @throws Exception
     */
    public Command execute(Command cmd) throws Exception;

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
	
	/**
	 * Fill in user properties by given http request
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public User transformRequestToUser(HttpServletRequest request) throws BeanValidateException;
	
	/**
	 * Fill in usergroup properties by given http request
	 * 
	 * @param request
	 * @return
	 */
	public UserGroup transformRequestToUserGroup(HttpServletRequest request) throws BeanValidateException;
}
