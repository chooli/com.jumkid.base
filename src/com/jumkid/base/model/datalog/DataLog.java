package com.jumkid.base.model.datalog;
/* 
 * This software is written by Jumkid Innovation and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * http://www.Jumkid.com
 * mailto:info@Jumkid.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 *
 * VERSION   |    DATE      |     DEVELOPER     |    DESC
 * -----------------------------------------------------------
 * 1.3.1       Mar2009            chooli             creation
 *
 */
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.jumkid.base.model.CommonBean;

@Entity
@Table (name = "datalog")
public class DataLog extends CommonBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2488594192796665143L;
	
	@Column (name = "action")
	private String action;	
	
	@Column (name = "module")
	private String module;
	
	@Column (name = "parent_module")
	private String parentModule;

	@Column (name = "object")
	private String object;  // object json or xml     
	
	@Column (name = "key_field_changes")
    private String keyFieldChanges; // key field changes xml
	
	@Column (name = "object_id")
    private String objectId;
	
	@Column (name = "activity_task")
    private Integer activityTask;
	
    public String getKeyFieldChanges() {
		return keyFieldChanges;
	}

	public void setKeyFieldChanges(String keyFieldChanges) {
		if(keyFieldChanges!=null) this.keyFieldChanges += keyFieldChanges;
		else this.keyFieldChanges = keyFieldChanges;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Integer getActivityTask() {
		return activityTask;
	}

	public void setActivityTask(Integer activityTask) {
		this.activityTask = activityTask;
	}

	public String getAction() {
		return action;
	}
	
    public void setAction(String action) {
		this.action = action;
	}
	
    
    public String getModule() {
		return module;
	}
	
    public void setModule(String module) {
		this.module = module;
	}
	
    public String getObject() {
		return object;
	}
	
    public void setObject(String object) {
		this.object = object;
	}
    
    public String getParentModule() {
		return parentModule;
	}

	public void setParentModule(String parentModule) {
		this.parentModule = parentModule;
	}
    
}
