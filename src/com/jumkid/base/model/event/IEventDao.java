package com.jumkid.base.model.event;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.jumkid.base.model.ICommonBeanDao;

public interface IEventDao extends ICommonBeanDao{

	/**
	 * 
	 * @param module
	 * @return
	 */
	public DetachedCriteria createDetachedCriteriaForModule(String module);
	
	
	/**
	 * 
	 * @param targetRefId
	 * @return
	 */
	public DetachedCriteria createDetachedCriteriaForTarget(String targetRefId);
	
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
	public List<Event> getEntityList(DetachedCriteria criteria, Integer start, Integer limit, String sortCriteria, String sortOrder) throws Exception;
	
}
