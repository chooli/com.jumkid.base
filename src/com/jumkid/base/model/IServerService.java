package com.jumkid.base.model;

public interface IServerService {

	/**
	 * 
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
    public Command execute(Command cmd) throws Exception;
    
}
