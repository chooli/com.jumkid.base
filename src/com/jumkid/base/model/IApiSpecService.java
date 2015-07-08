package com.jumkid.base.model;
/* 
 * This software is written by Jumkid Innovation. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * http://www.jumkid.com
 * mailto:support@jumkid.com
 *
 * (c)2013 Jumkid Ltd. All rights reserved.
 *
 */

public interface IApiSpecService {
	
	/**
	 * 
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
    public Command execute(Command cmd) throws Exception;

}
