package com.jumkid.base.model.usergroup;
/* 
 * This software is written by ClientFills and subject
 * to a contract between ClientFills and its customer.
 *
 * This software stays property of ClientFills unless differing
 * arrangements between ClientFills and its customer apply.
 *
 *
 * (c)2013 ClientFills All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0        Jan2013      chooli      creation
 * 
 *
 */

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import com.jumkid.base.model.AbstractHibernateDAO;

public class UserGroupDao extends AbstractHibernateDAO<UserGroup> implements IUserGroupDao {
	
	public UserGroupDao() {
		super(UserGroup.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserGroup> loadRootGroups()
    {

        DetachedCriteria criteria = DetachedCriteria.forClass(UserGroup.class).add(Property.forName("parentGroup").isNull() );
        List<UserGroup> list = this.findByCriteria( criteria);
        
        return list;
    }

}
