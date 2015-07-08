package com.jumkid.base.mail;
/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * Jumkid Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * 
 * Tel: +852 8199 9605
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 *
 * VERSION     |    DATE      |   DEVELOPER  |  DESC
 * ----------------------------------------------------------------
 * 1.0b625        Jun2008        chooli         creation
 *
 */

import com.jumkid.base.model.Command;

public interface IMailService {

	public Command execute(Command cmd) throws Exception;	
		
}
