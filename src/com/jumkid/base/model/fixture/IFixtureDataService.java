package com.jumkid.base.model.fixture;

import javax.servlet.http.HttpServletRequest;

import com.jumkid.base.exception.SystemServiceException;
import com.jumkid.base.model.Command;

public interface IFixtureDataService {
	
	/**
     * 
     * @param cmd
     * @return
     * @throws Exception
     */
    public Command execute(Command cmd) throws SystemServiceException;

    public FixtureData transformRequestToFixtureData(HttpServletRequest request) throws Exception;

}
