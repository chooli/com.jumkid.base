package com.jumkid.base.model.event;

import javax.servlet.http.HttpServletRequest;

import com.jumkid.base.model.Command;

public interface IEventService {

	/**
	 * 
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
    public Command execute(Command cmd) throws Exception ;
    
    /**
     * 
     * @param type
     * @param module
     * @param message
     * @param targetRefId
     * @return
     */
    public Event generateEvent(String type, String module, String message, String targetRefId);
    
    /**
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public Event transformRequestToEvent(HttpServletRequest request) throws Exception;
    
}
