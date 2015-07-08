package com.jumkid.base.model.event;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.jumkid.base.model.CommonBean;

@Entity
@Table (name = "event")
public class Event extends CommonBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8026748534740368467L;
	
	@Column (name = "type")
	private String type;
	
	@Column (name = "module")
	private String module;
	
	@Column (name = "message")
	private String message;
	
	@Column (name = "target_ref_id")
	private String targetRefId;
	
	@Column (name = "alarm")
	private Timestamp alarm;
	
	@Column (name = "fired")
	private boolean fired;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTargetRefId() {
		return targetRefId;
	}

	public void setTargetRefId(String targetRefId) {
		this.targetRefId = targetRefId;
	}

	public Timestamp getAlarm() {
		return alarm;
	}

	public void setAlarm(Timestamp alarm) {
		this.alarm = alarm;
	}

	public boolean getFired() {
		return fired;
	}

	public void setFired(boolean fired) {
		this.fired = fired;
	}
	

}
