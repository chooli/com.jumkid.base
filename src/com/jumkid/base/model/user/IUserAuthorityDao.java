package com.jumkid.base.model.user;

import java.util.List;

public interface IUserAuthorityDao {

	/**
	 * 
	 * @param userName
	 * @return
	 */
    public List<UserAuthority> loadByUser(User user);
    
}
