/** 
 * This software is written by jumkid Ltd. and subject
 * to a contract between jumkid and its customer.
 *
 * This software stays property of jumkid unless differing
 * arrangements between jumkid and its customer apply.
 *
 * jumkid Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * 
 * Tel: +852 8199 9605
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2008 jumkid Ltd. All rights reserved.
 *
 * VERSION     |    DATE      |   DEVELOPER  |  DESC
 * ----------------------------------------------------------------
 * 1.0b615       Jun2008        chooli         creation
 * 1.0b715       july2008       chooli         support multi site
 * 1.3.1         Mar2009        chooli         revise datalog architecture
 */

package com.jumkid.base.controller.log;

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
import com.jumkid.base.model.datalog.IDataLogService;
import com.jumkid.base.exception.PermissionDeniedException;
import com.jumkid.base.util.Constants;

@Controller
public class LogController{
	
	protected final Log logger = LogFactory.getLog(this.getClass());	
        
    private IDataLogService dataLogService;
        
    /**
     * Handles the request to this controller
     * @param request  is the current http servlet request
     * @param response is the current http servlet response
     * @return         the model and view 
     * @see {@link ModelAndView} 
     */
    public ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PermissionDeniedException {
        
        HashMap<String, Object> params = new HashMap<String, Object>();
                
        String act = request.getParameter("act").toLowerCase();        
        
        ServiceSession sSession = new ServiceSession(request);
        params.put(ServiceSession.USER_SESSION_KEY, sSession.getUser().getId());
        Command cmd = null;
        
        try {
        	if (act.equals("list")) {
                Integer start = request.getParameter("start")==null?null:Integer.valueOf(request.getParameter("start"));
                Integer limit = request.getParameter("limit")==null?null:Integer.valueOf(request.getParameter("limit"));                                                
                params.put("start", start);
                params.put("limit", limit);                
                params.put("sortOrder", request.getParameter("sortOrder"));                
                params.put("sortCriteria", request.getParameter("sortCriteria"));  
                params.put("module", request.getParameter("module"));
                params.put("parentModule", request.getParameter("parentModule"));
                Integer objectId = request.getParameter("objectId")==null?null:Integer.valueOf(request.getParameter("objectId"));
                params.put("objectId", objectId);
                
                cmd = dataLogService.execute( new Command("logmanager", act, params) ); 
                
                sSession.wrapCommand(cmd, "datalogs", true);
                
            }else 
            if (act.equals("load")) {                   	
            	String _id = request.getParameter("id");
        		if(_id!=null){
        			params.put("id", Integer.valueOf(_id));

                	cmd = dataLogService.execute( new Command("logmanager", act, params) );
                	
                	sSession.wrapCommand(cmd, "datalog", false);                 
        		}else{
        			sSession.wrapCommand(new Command(Constants.CMSG_INVALID_ARGUMENT));
        		}  
            }
            
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            sSession.setErrors(e.getMessage());
        }
        
        return new ModelAndView(sSession.getSuccessView(), "model", sSession);
        
    }

	public IDataLogService getDataLogService() {
		return dataLogService;
	}

	public void setDataLogService(IDataLogService dataLogService) {
		this.dataLogService = dataLogService;
	}

	
}
