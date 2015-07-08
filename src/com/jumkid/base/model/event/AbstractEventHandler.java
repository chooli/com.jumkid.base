package com.jumkid.base.model.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractEventHandler {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private IEventDao eventDao;
	
	private String eventType;
	
	private String eventModule;
	
	private String eventMessage;
	
	private String targetRefId;
	
	public abstract void fire(Event event) throws Exception;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventModule() {
		return eventModule;
	}

	public void setEventModule(String eventModule) {
		this.eventModule = eventModule;
	}

	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}

	public String getTargetRefId() {
		return targetRefId;
	}

	public void setTargetRefId(String targetRefId) {
		this.targetRefId = targetRefId;
	}

	public IEventDao getEventDao() {
		return eventDao;
	}

	public void setEventDao(IEventDao eventDao) {
		this.eventDao = eventDao;
	}
	
	
	
}
