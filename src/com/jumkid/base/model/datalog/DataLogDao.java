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
 * Tel: +852 8199 9605
 * http://www.Jumkid.com
 * mailto:info@Jumkid.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 *
 * VERSION   |    DATE      |     DEVELOPER     |    DESC
 * -----------------------------------------------------------------
 * 1.3.1       Mar2009            chooli             creation
 *
 */
 
import java.sql.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jumkid.base.model.AbstractHibernateDAO;
import com.jumkid.base.rest.security.SecureUserDetailsService;
import com.jumkid.base.util.Constants;

public class DataLogDao extends AbstractHibernateDAO<DataLog> implements IDataLogDao{
	
	private SecureUserDetailsService secureUserDetailsService;

	public DataLogDao() {
		super(DataLog.class);
	}
	
	public DetachedCriteria createDetachedCriteria(String module, String objectId) {
        // init filter and sort criteria
        DetachedCriteria criteria = DetachedCriteria.forClass(DataLog.class);        
        if(module!=null && !module.isEmpty()){
        	criteria.add(Restrictions.eq("module", module));
        }
        if(objectId!=null && !objectId.isEmpty()){
        	criteria.add(Restrictions.eq("object_id", objectId));
        }
        
        return criteria;
    }
	
	@Override
	public DetachedCriteria createDetachedCriteria(String module,
			Date dateBefore, String action) {
		
        DetachedCriteria criteria = DetachedCriteria.forClass(DataLog.class);        
        if(module!=null && !module.isEmpty()){
        	criteria.add(Restrictions.eq("module", module));
        }
        if(dateBefore!=null){
        	criteria.add(Restrictions.ge("createdOn", dateBefore));
        }
        if(action!=null && !action.isEmpty()){
        	criteria.add(Restrictions.eq("action", action));
        }
        
        return criteria;
	}	
	
	
	@SuppressWarnings("unchecked")
	public List<DataLog> getDataLogList(DetachedCriteria criteria, Integer start, Integer limit, String sortCriteria, String sortOrder) throws Exception{
		List<DataLog> results = null;        
        
        // sorting
        if (sortCriteria!=null && Constants.SORTORDER_ASC.equals(sortOrder)) criteria.addOrder(Order.asc(sortCriteria));
        else if (sortCriteria!=null && Constants.SORTORDER_DESC.equals(sortOrder)) criteria.addOrder(Order.desc(sortCriteria));
        else criteria.addOrder(Order.desc("createdOn"));
        
        criteria = applyPermission(criteria);
        
        // paging (optional)
        results = (start!=null && limit!=null) ? this.findByCriteria(criteria, start, limit) : this.findByCriteria(criteria);            
        
        return results;
	}
	
	private synchronized DetachedCriteria applyPermission(DetachedCriteria criteria){
		if(!isAdminUser()){
			org.springframework.security.core.userdetails.User _user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = _user.getUsername();
			
			criteria.add(Restrictions.eq("createdBy", username));
		}
		return criteria;
	}
	
	private boolean isAdminUser(){
		try{
			org.springframework.security.core.userdetails.User _user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = _user.getUsername();
			
			for(GrantedAuthority authority:secureUserDetailsService.loadUserByUsername(username).getAuthorities()){
				if("ROLE_ADMIN".equals(authority.getAuthority())){
					return true;
				}
			}
		}catch(Exception e){
			//void
		}
				
		return false;
	}

	public SecureUserDetailsService getSecureUserDetailsService() {
		return secureUserDetailsService;
	}

	public void setSecureUserDetailsService(
			SecureUserDetailsService secureUserDetailsService) {
		this.secureUserDetailsService = secureUserDetailsService;
	}
	
}
