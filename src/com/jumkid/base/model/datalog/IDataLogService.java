package com.jumkid.base.model.datalog;
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
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 *
 * VERSION   |    DATE      |     DEVELOPER     |    DESC
 * -----------------------------------------------------------
 * 1.3.1       Mar2009            chooli             creation
 * 
 */

import com.jumkid.base.model.datalog.DataLog;
import com.jumkid.base.model.Command;
import com.jumkid.base.model.AbstractBean;

public interface IDataLogService
{
    
	/**
	 * 
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
    public Command execute(Command cmd) throws Exception ;
    
    /**
     * 
     * @param saveObject
     * @param existObject
     * @param datalog
     * @return
     */
    public DataLog diffObjectChanges(AbstractBean saveObject, AbstractBean existObject, DataLog datalog);
    
}
