package com.jumkid.base.controller.event;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jumkid.base.model.Command;
import com.jumkid.base.model.ServiceSession;
import com.jumkid.base.model.event.Event;
import com.jumkid.base.model.event.IEventService;

@Controller
public class EventController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private IEventService eventService;
	
	/**
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/event/load/{id}", method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	private String load(@PathVariable("id") Integer id, HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(false);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		
		try {
			Command cmd = eventService.execute( new Command("eventmanager", "load", params) );
			
			return sSession.wrapCommand(cmd, "event", false).getJsonResult();
			
		} catch (Exception e) {
	       	sSession.setErrors( e.getMessage()!=null?e.getMessage():e.getClass().getName() );
	    }
		
		return sSession.toServiceJSONResult();
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/event/save", method=RequestMethod.POST, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	private String save(HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(false);
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			Event event = eventService.transformRequestToEvent(request);
			params.put("event", event);
					
			Command cmd = eventService.execute( new Command("eventmanager", "save", params) );
			
			return sSession.wrapCommand(cmd, "event", false).getJsonResult();
			
		} catch (Exception e) {
	       	sSession.setErrors( e.getMessage()!=null?e.getMessage():e.getClass().getName() );
	    }
		
		return sSession.toServiceJSONResult();
		
		
	}
	
	/*
	 * 
	 */
	@RequestMapping(value="/event/list", method=RequestMethod.POST, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	private String list(HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(false);
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			params.put("module", request.getParameter("module"));
			String _start = request.getParameter("start");
			Integer start = _start!=null ? Integer.parseInt(_start):0;
			String _limit = request.getParameter("limit");
			Integer limit = _limit!=null ? Integer.parseInt(_limit):20;
			params.put("start", start);
			params.put("limit", limit);
					
			Command cmd = eventService.execute( new Command("eventmanager", "list", params) );
			
			return sSession.wrapCommand(cmd, "events", true).getJsonResult();
			
		} catch (Exception e) {
	       	sSession.setErrors( e.getMessage()!=null?e.getMessage():e.getClass().getName() );
	    }
		
		return sSession.toServiceJSONResult();
	}
	
	/**
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/event/fire/{id}", method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	private String fire(@PathVariable("id") Integer id, HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(false);
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			params.put("id", id);
					
			eventService.execute( new Command("eventmanager", "fire", params) );
			
			sSession.setSuccess(true);
			
		} catch (Exception e) {
	       	sSession.setErrors( e.getMessage()!=null?e.getMessage():e.getClass().getName() );
	    }
		
		return sSession.toServiceJSONResult();
	}

	public IEventService getEventService() {
		return eventService;
	}
	
	/**
	 * 
	 * @param uuid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/event/remove/{id}", method=RequestMethod.DELETE, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	public String remove(@PathVariable("id") Integer id, HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(false);
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
				        
        try{
        	Command cmd = eventService.execute(new Command("eventmanager", "delete", params));
        	
        	return sSession.wrapCommand(cmd).getJsonResult();
        	
        }catch (Exception e) {
        	sSession.setErrors(e.getLocalizedMessage());
        }
        return sSession.toServiceJSONResult();
	}

	public void setEventService(IEventService eventService) {
		this.eventService = eventService;
	}

}
