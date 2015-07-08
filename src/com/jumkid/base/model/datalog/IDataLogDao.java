package com.jumkid.base.model.datalog;

import java.sql.Date;
import java.util.List;


/* 
 * This software is written by SYSVISION Ltd. and subject
 * to a contract between SYSVISION and its customer.
 *
 * This software stays property of SYSVISION unless differing
 * arrangements between SYSVISION and its customer apply.
 *
 * SYSVISION Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * 
 * Tel: +852 8199 9605
 * http://www.sysvision.com
 * mailto:info@sysvision.com
 *
 * (c)2008 SYSVISION Ltd. All rights reserved.
 *
 * VERSION   |    DATE      |     DEVELOPER     |    DESC
 * -----------------------------------------------------------------
 * 1.3.1       Mar2009            chooli             creation
 *
 */
import org.hibernate.criterion.DetachedCriteria;

import com.jumkid.base.model.ICommonBeanDao;

public interface IDataLogDao extends ICommonBeanDao {

	/**
	 * 
	 * @param module
	 * @return
	 */
	public DetachedCriteria createDetachedCriteria(String module, String objectId);
	
	/**
	 * 
	 * @param module
	 * @return
	 */
	public DetachedCriteria createDetachedCriteria(String module, Date dateBefore, String action);

	/**
	 * 
	 * @param criteria
	 * @param start
	 * @param limit
	 * @param sortCriteria
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<DataLog> getDataLogList(DetachedCriteria criteria, Integer start, Integer limit, String sortCriteria, String sortOrder) throws Exception;
	
    
}
