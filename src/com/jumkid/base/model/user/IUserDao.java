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
 * 3.0        Jan2013      chooli      creation
 * 
 *
 */

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.jumkid.base.model.ICommonBeanDao;

public interface IUserDao extends ICommonBeanDao{


	/**
	 * 
	 * @param userName
	 * @return
	 */
    public User loadByUserName(String userName);
    
    /**
     * 
     * @param username
     * @param password
     * @return
     */
    public User loadByUsernameAndPassword(String username, String password);
    
    /**
     * 
     * @param username
     * @return
     */
    public User loadByEmail(String email);
    
    
    /**
     * 
     * @param vid
     * @return
     */
    public List<User> loadAllByGroupIds(Integer groupId);
    
    /**
     * 
     * @param keyword
     * @return
     */
    public DetachedCriteria createDetachedCriteria(String keyword);
    
    /**
     * 
     * @param keyword
     * @return
     */
    public DetachedCriteria createDetachedCriteria(String keyword, String scope);
    
        
}
