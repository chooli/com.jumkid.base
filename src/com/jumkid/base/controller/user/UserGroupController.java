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
 * (c)2008 Jumkid Ltd. All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0          march2008   mathias      creation
 * 1.0b715     july2008     chooli        support multi site
 *
 */
package com.jumkid.base.controller.user;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.jumkid.base.model.Command;
import com.jumkid.base.model.ServiceSession;
import com.jumkid.base.model.user.IUserService;
import com.jumkid.base.util.Constants;

// logging is disabled, uncomment to enable logging
@Controller
public class UserGroupController {    
	
	protected final Log logger = LogFactory.getLog(this.getClass());	
    
    private IUserService userService;
    
    public ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        HashMap<String, Object> params = new HashMap<String, Object>();
        
        String act = request.getParameter("act").toLowerCase();

        ServiceSession sSession = new ServiceSession(request);
        params.put(ServiceSession.USER_SESSION_KEY, sSession.getUser().getId());
        Command cmd = null;

        try {        	
        	
        	if ("tree".equals(act)) {                
                cmd = userService.execute( new Command("usergroupmanager", act, params) );
                
                sSession.wrapCommand(cmd, "usergroupTreeXml", ServiceSession.FORMAT_XML);
            }else
            if ("list".equals(act)) {               
                cmd = userService.execute( new Command("usergroupmanager", act, params) );
                
                sSession.wrapCommand(cmd, "usergroups", true);
                
            } else
            if ("load".equals(act) || "delete".equals(act)) {
            	String _id = request.getParameter("id");
        		if(_id!=null){
        			params.put("id", Integer.valueOf(_id));
        			cmd = userService.execute( new Command("usergroupmanager", act, params) );
                    
                    if("load".equals(act))
                    	sSession.wrapCommand(cmd, "usergroup", false);
                    else
                    	sSession.wrapCommand(cmd);                  
        		}else{
        			sSession.wrapCommand(new Command(Constants.CMSG_INVALID_ARGUMENT));
        		}                  
                
            } else 
            if ("save".equals(act)) {            	
                params.put("userGroup", userService.transformRequestToUserGroup(request));                
                
                cmd = userService.execute( new Command("usergroupmanager", act, params) );
                
                sSession.wrapCommand(cmd, "usergroup", false);
            }
            
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            sSession.setErrors(e.getMessage());
        }
        
        return new ModelAndView(sSession.getSuccessView(), "model", sSession);

    }


    public IUserService getUserService() {
        return userService;
    }


    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
	
}