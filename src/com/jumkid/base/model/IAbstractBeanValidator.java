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
 * VERSION   |   DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0         July2013       chooli       creation
 * 
 *
 */

import com.jumkid.base.exception.BeanValidateException;

public interface IAbstractBeanValidator {
	
	public static final String VTYPE_EMPTY = "empty";
	public static final String VTYPE_DUPLICATE = "duplicate";

	public IAbstractBeanValidator validate(String vtype, String expression, AbstractBean bean) throws BeanValidateException;
	
	public IAbstractBeanValidator validate(String vtype, String propertyName, Object beanValue, ICommonBeanDao objectDAO) throws BeanValidateException;
	
	public IAbstractBeanValidator validate(String vtype, String propertyName, AbstractBean bean, ICommonBeanDao objectDAO) throws BeanValidateException;
	
}
