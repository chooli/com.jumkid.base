package com.jumkid.base.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jumkid.base.model.ServiceSession;

public class UserLocaleInterceptor extends HandlerInterceptorAdapter {

	protected final Log logger = LogFactory.getLog(UserLocaleInterceptor.class);
	 
	//before the actual handler will be executed
	public boolean preHandle(HttpServletRequest request, 
								HttpServletResponse response, Object handler)
							    throws Exception {		
		String _locale = request.getParameter("locale");		
		Locale locale =  new Locale( (_locale==null || _locale.isEmpty()) ? ServiceSession.getLocale(request):_locale );                                
		ServiceSession.setLocale(request, locale);		
		request.setAttribute("locale", locale.toString().toLowerCase());
 
		return true;
	}
 
	/*after the handler is executed
	public void postHandle(
		HttpServletRequest request, HttpServletResponse response, 
		Object handler, ModelAndView modelAndView)
		throws Exception {
 
		long startTime = (Long)request.getAttribute("startTime");
 
		long endTime = System.currentTimeMillis();
 
		long executeTime = endTime - startTime;
 
		//modified the exisitng modelAndView
		modelAndView.addObject("executeTime",executeTime);
 
		//log it
		if(logger.isDebugEnabled()){
		   logger.debug("[" + handler + "] executeTime : " + executeTime + "ms");
		}
	}
	*/
}