package com.jumkid.base.rest.security;

/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * 
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2014 Jumkid Innovation. All rights reserved.
 *
 *
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
* Authentication filter for REST services
*/
public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Override
	protected boolean requiresAuthentication(HttpServletRequest request,
										HttpServletResponse response) {
		boolean retVal = false;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		logger.debug("authenticate username="+username+", password="+password);
		
		if (username != null && password != null) {
			Authentication authResult = null;
			try {
				authResult = attemptAuthentication(request, response);
				if (authResult == null) {
					retVal = false;
				}
			} catch (AuthenticationException failed) {
				
				try {
					unsuccessfulAuthentication(request, response, failed);
				} catch (IOException e) {
					retVal = false;
				} catch (ServletException e) {
					retVal = false;
				}
				
				retVal = false;
			}
			try {
				this.successfulAuthentication(request, response, null, authResult);
				//successfulAuthentication(request, response, authResult);
			} catch (IOException e) {
				retVal = false;
			} catch (ServletException e) {
				retVal = false;
			}
			
			return false;
		
		} else {
			retVal = true;
		}
		return retVal;
	}
}
