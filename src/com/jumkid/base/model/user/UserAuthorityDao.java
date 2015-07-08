package com.jumkid.base.model.user;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.jumkid.base.model.AbstractHibernateDAO;

public class UserAuthorityDao extends AbstractHibernateDAO<UserAuthority> implements IUserAuthorityDao {

	public UserAuthorityDao() {
		super(UserAuthority.class);
	}

	@Override
	public List<UserAuthority> loadByUser(User user) {
		try {            
			DetachedCriteria criteria = DetachedCriteria.forClass(UserAuthority.class)
					.add(Restrictions.eq("user", user));
			
			@SuppressWarnings("unchecked")
			List<UserAuthority> list = this.findByCriteria(criteria);
						
	        return list;
	        
        } catch (HibernateException ex) {
            logger.error("failed to load user by name "+ex.getMessage());
            return null;
        } 
	}

}
