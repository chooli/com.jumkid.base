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
 * 0.1        Jan2013      chooli      creation
 * 
 *
 */


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.jumkid.base.model.user.User;
import com.jumkid.base.model.CommonBean;
import com.jumkid.base.util.Formatter;

public class UserGroup extends CommonBean implements java.io.Serializable{

	/**
	 * The serial version UID of this class. Needed for serialization.
	 */
	private static final long serialVersionUID = 3906644185426965040L;
	
	public static final String PROPERTY_GROUPNAME = "groupName";
	public static final String PROPERTY_GROUPPATH = "groupPath";
	public static final String PROPERTY_PARENTGROUPID = "parentGroupId";
	public static final String PROPERTY_DESCRIPTION = "description";
	public static final String PROPERTY_USERS = "users";
	
	private String groupName;
	private String description;
	private Integer statusId;
	private Set<User> users;
	
	private UserGroup parentGroup;
    private Integer parentGroupId;  // not persistent, just helper property
    
	private Set<UserGroup> childGroups;
	
	public Set<User> getUsers() {
	    return users;
	}
	
	public Set<String> getUserNames(){
		Set<User> users = this.getUsers();
		Set<String> userNames = new TreeSet<String>();
		for(Iterator<User> itr = users.iterator();itr.hasNext();) userNames.add(((User)itr.next()).getUsername());
		return userNames;
	}	
	
	@SuppressWarnings("unchecked")
	public Set<User> getSubordinateUsers() {
    	return getSubordinateUserObjects("OBJECT");
    }
    
    @SuppressWarnings("unchecked")
	public Set<Integer> getSubordinateUserIDs() {
    	return getSubordinateUserObjects("ID");
    }
    
    @SuppressWarnings("unchecked")
	public Set<String> getSubordinateUserNames() {
    	return getSubordinateUserObjects("NAME");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Set getSubordinateUserObjects(String mode) {    	
    	Set subUsers = new TreeSet();
        for (Iterator<UserGroup> childGroups=this.childGroups.iterator();childGroups.hasNext();) {
            UserGroup childGroup = (UserGroup)childGroups.next();
            for (Iterator<User> users = childGroup.getUsers().iterator();users.hasNext();) {
            	if(mode.equals("OBJECT")) subUsers.add((User)users.next());
            	else if(mode.equals("ID")) subUsers.add(((User)users.next()).getId());
            	else if(mode.equals("NAME")) subUsers.add(((User)users.next()).getUsername());
            }
            subUsers.addAll(childGroup.getSubordinateUserObjects(mode));
        }
        return subUsers;
    }
	
	public void setUsers(Set<User> users) {
	    this.users = users;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getGroupName() {
	    return groupName;
	}
	/**
	 * @param name The name to set.
	 */
	public void setGroupName(String groupname) {
	    this.groupName = groupname;
	}
	
	public String getDescription() {
	    return description;
	}
	
	public void setDescription(String description) {
	    this.description = description;
	}
	
	public String toJSON(){
		String json = "{";
        
        json += Formatter.toJSONString(PROPERTY_ID, Formatter.TYPE_JSON_NUMBER, 
        		getId(), false);
        json += Formatter.toJSONString(PROPERTY_GROUPNAME, Formatter.TYPE_JSON_STRING, 
        		this.getGroupName(), false);
        json += Formatter.toJSONString(PROPERTY_PARENTGROUPID, Formatter.TYPE_JSON_NUMBER, 
        		(this.getParentGroup()==null?null:this.getParentGroup().getId()), false);
        json += Formatter.toJSONString(PROPERTY_GROUPPATH, Formatter.TYPE_JSON_STRING, 
        		this.getGroupPath(), false);
        
        if(this.users!=null && !this.users.isEmpty()){
        	json += "\""+PROPERTY_USERS+"\":[";
    		for (Iterator<User> i = this.users.iterator();i.hasNext();) {
    			User user = i.next();
    			json += user.toJSON() + (i.hasNext()?",":"");
    		}		
            json += "],";
        }

        json += Formatter.toJSONString(PROPERTY_DESCRIPTION, Formatter.TYPE_JSON_STRING, 
        		this.getDescription(), true);
        
        json += "}";        
        
        return json;
	}
	
    public String getGroupPath() {
        String path = "";        
        path = (getParentGroup()==null?"":getParentGroup().getGroupPath()) + "/" + this.getGroupName(); 
        return path;
    }  

	public UserGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(UserGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public Integer getParentGroupId() {
		return parentGroupId;
	}

	public void setParentGroupId(Integer parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	public Set<UserGroup> getChildGroups() {
		return childGroups;
	}

	public void setChildGroups(Set<UserGroup> childGroups) {
		this.childGroups = childGroups;
	}

	@Override
	public String[] getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

}