package com.jumkid.base.rest.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jumkid.base.model.user.IUserDao;
import com.jumkid.base.model.user.UserAuthority;

public class SecureUserDetailsService implements UserDetailsService {
	
	protected final Log logger = LogFactory.getLog(this.getClass());	
	
	private IUserDao userDao;

	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {
		com.jumkid.base.model.user.User user = userDao.loadByUserName(username);
		if(user!=null){
			List<GrantedAuthority> authorities = 
                    buildUserAuthority(user.getUserAuthority());

			return buildUserForAuthentication(user, authorities);
		}
		
		return null;
	}
	
	// Converts com.jumkid.site.model.User user to
	// org.springframework.security.core.userdetails.User
	private User buildUserForAuthentication(com.jumkid.base.model.user.User user, 
		List<GrantedAuthority> authorities) {
		//return new User(user.getUsername(), user.getPassword(), 
			//user.getActivated(), true, true, true, authorities);
		return new User(user.getUsername(), user.getPassword(), 
				true, true, true, true, authorities);
	}
 
	private List<GrantedAuthority> buildUserAuthority(UserAuthority userAuthority) {
 
		List<GrantedAuthority> setAuths = new ArrayList<GrantedAuthority>();
 
		// Build user's authorities
		setAuths.add(new SimpleGrantedAuthority(userAuthority.getAuthority()));
 
		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
 
		return Result;
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

}
