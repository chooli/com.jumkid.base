package com.jumkid.base.model.event;

public interface AbstractEventHandlerFactory {
	
	public AbstractEventHandler createEventHandler(Event event) throws Exception;

}
