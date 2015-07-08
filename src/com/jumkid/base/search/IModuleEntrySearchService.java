package com.jumkid.base.search;
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
 * VERSION   |   DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0         July2013       chooli       creation
 * 
 *
 */
import com.jumkid.base.model.Command;

public interface IModuleEntrySearchService {

	/**
     * 
     * @param cmd
     * @return
     * @throws Exception
     */
    public Command execute(Command cmd) throws Exception;
    
}
