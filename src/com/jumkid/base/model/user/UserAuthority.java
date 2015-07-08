package com.jumkid.base.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.jumkid.base.model.CommonBean;

@Entity
@Table (name = "user_authority")
public class UserAuthority extends CommonBean {
	
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_AGENT = "ROLE_AGENT";

	/**
	 * 
	 */
	private static final long serialVersionUID = -4822686999989931286L;
	
	@Column (name = "authority")
	private String authority;
	
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
