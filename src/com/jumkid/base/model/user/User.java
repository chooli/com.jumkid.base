package com.jumkid.base.model.user;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jumkid.base.model.CommonBean;
import com.jumkid.base.model.usergroup.UserGroup;

@Entity
@Table (name = "userdata")
public class User extends CommonBean  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4835810838348512716L;

	@Column (name = "username")
	private String username;
	
	@Column (name = "fullname")
    private String fullName;
	
	@Column (name = "email")
    private String email;
    
	@Column (name = "password")
    private String password;    
    
	@Column (name = "last_login_date")
    private Timestamp lastLoginDate;
    
	@Column (name = "failed_login_attempts")
    private Integer failedLoginAttempts;  
    
	@Column (name = "activated")
    private Boolean activated;
	
	@Column (name = "avatar")
	private String avatar;
    
	@Transient
    private UserGroup userGroup;
	@Transient
    private Integer userGroupId; // not persistent, just helper property 
    
	@OneToOne(fetch = FetchType.LAZY, mappedBy="user", cascade=CascadeType.ALL)
    private UserAuthority userAuthority;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(
		name = "user_relationship", 
		joinColumns = {
				@JoinColumn(name = "user_id", nullable = false)
		},
		inverseJoinColumns = { 
				@JoinColumn(name = "friend_id", nullable = false) 
		}
	)
	private Set<User> friends;
	
	public Set<User> getFriends() {
		return friends;
	}
	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}
	public void addFriend(User user){
		if(this.friends==null){
			friends = new HashSet<User>();
		}
		friends.add(user);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Integer getFailedLoginAttempts() {
		return failedLoginAttempts;
	}
	public void setFailedLoginAttempts(Integer failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}
	public Boolean getActivated() {
		return activated;
	}
	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
	
	public UserAuthority getUserAuthority() {
		return userAuthority;
	}
	public void setUserAuthority(UserAuthority userAuthority) {
		this.userAuthority = userAuthority;
	}
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	public Integer getUserGroupId() {
		return userGroupId;
	}
	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
        
	
}
