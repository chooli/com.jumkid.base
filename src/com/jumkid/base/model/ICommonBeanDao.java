package com.jumkid.base.model;

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
 * 3.0        May2013      chooli      creation
 * 
 *
 */

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;


import com.jumkid.base.exception.ConcurrentModificationException;

public interface ICommonBeanDao {
	
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria dc);
	
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria dc, Integer start, Integer limit);
	
	public <T> List<T> find(String hql);
	
    public CommonBean load(Integer id);
    
    public <T> List<T> load(DetachedCriteria criteria, Integer start, Integer limit, String sortCriteria, String sortOrder);
    
    public <T> List<T> loadAll();
    
    public <T> List<T> loadAll(Integer start, Integer limit);
	
	public <T> int getRowCount(DetachedCriteria criteria, Class<T> objectClass);   
	
	public CommonBean create(final CommonBean bean);
	
	public void update(final CommonBean bean) throws ConcurrentModificationException; 

	public void remove(Integer id) throws IllegalArgumentException;
	
	public void remove(CommonBean bean) throws IllegalArgumentException;
	
	public void deactivate(CommonBean bean) throws IllegalArgumentException;
    
    public void deactivate(Integer id) throws IllegalArgumentException;
    
}
