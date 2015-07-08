package com.jumkid.base.logging;
/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * Jumkid Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * 
 * Tel: +852 8199 9605
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2007 Jumkid Ltd. All rights reserved.
 *
 */
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * @author mathias.conradt
 *
 * The Logging Advice logs method calls with method name
 * and parameter values.
 * Logging Advice is used as an interceptor.
 */
public class LoggingAdvice implements MethodBeforeAdvice {

	
	
	/* (non-Javadoc)
	 * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 */
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		
		Logger logger = Logger.getLogger(arg2.getClass());
		
		String paramTypes = "";		
		if (arg0!=null && arg0.getParameterTypes()!=null) {
			for (int i=0;i<=arg0.getParameterTypes().length-1;i++) {
				paramTypes += arg0.getParameterTypes()[i].getName();
				if (i<arg0.getParameterTypes().length-1) paramTypes += ", "; 
			}
		}
		
		String paramValues = "";
		if (arg1!=null && arg1.length>0) {
			for (int i=0;i<=arg1.length-1;i++) {
			    try {
				paramValues += 
					(arg1[i].getClass().getName()=="java.lang.String"?"\'":"") + 
					arg1[i].toString() + 
					(arg1[i].getClass().getName()=="java.lang.String"?"\'":"");
			    } catch (NullPointerException e) {
			        // e.printStackTrace();
			        // logger.error(e.getStackTrace()[0]);
			        paramValues += "unknown caused by exception";
			    }
				if (i<arg1.length-1) paramValues += ", "; 
			}
		}
		
		logger.info( "Execute " + arg0.getName() + "(" + paramTypes + ")" );
		if (paramValues!=null && !"".equals(paramValues)) {
			logger.info( "Values " + "(" + paramValues + ")" );
		}
		
	}
	

}
