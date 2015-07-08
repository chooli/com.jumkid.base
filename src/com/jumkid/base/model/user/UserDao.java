package com.jumkid.base.model.user;
/* 
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of ClientFills unless differing
 * arrangements between Jumkid and its customer apply.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jumkid.base.model.AbstractHibernateDAO;
import com.jumkid.base.util.Constants;

public class UserDao extends AbstractHibernateDAO<User> implements IUserDao {
	
	public UserDao() {
		super(User.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public User loadByUserName(String username) {
		if (username== null) {
            throw new IllegalArgumentException("'username' can not be null");
        }
		try {            
			DetachedCriteria criteria = DetachedCriteria.forClass(User.class)
					.add(Restrictions.eq("username", username));
			
			List<User> list = this.findByCriteria(criteria);
			
			final Object entity = (list != null && !list.isEmpty()) ? list.iterator().next() : null;
			User user = (User)entity;
			Hibernate.initialize(user.getFriends());
	        return user;
	        
        } catch (HibernateException ex) {
            logger.error("failed to load user by name "+ex.getMessage());
            return null;
        } 
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public User loadByUsernameAndPassword(String username, String password) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(User.class)
					.add(Restrictions.eq("username", username))
					.add(Restrictions.eq("password", password));		
			
			List<User> list = this.findByCriteria(criteria);
			
			final Object entity = (list != null && !list.isEmpty()) ? list.iterator().next() : null;
	        return (User)entity;
	        
        } catch (HibernateException ex) {
        	logger.error("error: "+ex.getMessage());
        } catch(Exception e){
        	logger.error("error: "+e.getMessage());	
        }
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public User loadByEmail(String email) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(User.class)
					.add(Restrictions.eq("email", email));		
			
			List<User> list = this.findByCriteria(criteria);
			
			final Object entity = (list != null && !list.isEmpty()) ? list.iterator().next() : null;
	        return (User)entity;
	        
        } catch (HibernateException ex) {
        	logger.error("error: "+ex.getMessage());
        } catch(Exception e){
        	logger.error("error: "+e.getMessage());	
        }
		
		return null;
	}

	@Override
	public List<User> loadAllByGroupIds(Integer groupId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public DetachedCriteria createDetachedCriteria(String keyword) {
        return createDetachedCriteria(keyword, Constants.PRIVATE);
    }

	@Override
    public DetachedCriteria createDetachedCriteria(String keyword, String scope) {
        DetachedCriteria criteria = DetachedCriteria.forClass(this.entityClass);
        
        if (keyword!=null) {
        	Junction junction = Restrictions.disjunction()                     
	            .add(Restrictions.like("username", keyword, MatchMode.ANYWHERE).ignoreCase())                     
	            .add(Restrictions.like("email", keyword, MatchMode.ANYWHERE).ignoreCase());      	       
        	
            criteria.add(junction);
            
            //search by relationships
        	if(Constants.PRIVATE.equals(scope)){
        		org.springframework.security.core.userdetails.User _user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String username = _user.getUsername();
				User user = loadByUserName(username);
				
				Iterator<User> itr = user.getFriends().iterator();
				ArrayList<Integer> ids = new ArrayList<Integer>();
				while(itr.hasNext()){
					User auser = (User)itr.next();
					ids.add(auser.getId());
				}
								
				criteria.add(Restrictions.in("id", ids));        		

        	}
        	
        }
        
        return criteria;
    }    

}
