package com.jumkid.base.model.event;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jumkid.base.exception.BeanValidateException;
import com.jumkid.base.exception.SystemServiceException;
import com.jumkid.base.model.AbstractCommandService;
import com.jumkid.base.model.Command;
import com.jumkid.base.model.IAbstractBeanValidator;
import com.jumkid.base.model.event.Event;
import com.jumkid.base.util.Formatter;

public class EventService extends AbstractCommandService implements IEventService {
	
	private IEventDao eventDao;
	
	private IAbstractBeanValidator abstractBeanValidator;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	@Override
	public Command execute(Command cmd) throws Exception {
		super.execute(cmd);
    	
    	try {
	        if (isManager("eventmanager")) {
	            // load event
	            if (isAction("load")) {
	                Integer id = (Integer)cmd.getParams().get("id");
	                Event event = getEventById( id );
	                cmd.addResults( "event", event );
	                
	            }else 
	            // save event
	            if (isAction("save")) {                
	                Event event = (Event)cmd.getParams().get("event");
	                event = saveEvent(event);
	                cmd.addResults( "event", event );
	                
	            } else
	            // delete event
	            if (isAction("delete")) {
	                Integer id = (Integer)cmd.getParams().get("id");	                
                    deleteEvent(id);
	                           
	            } else
	            // list or search event
	            if (isAction("list")) {
	                Integer start = (Integer)cmd.getParams().get("start");
	                Integer limit = (Integer)cmd.getParams().get("limit");
	                String sortOrder = (String)cmd.getParams().get("sortOrder");
	                String sortCriteria = (String)cmd.getParams().get("sortCriteria");    
	                String module = (String)cmd.getParams().get("module");                
	                
	                DetachedCriteria criteria;
	                if(module!=null && module.trim().isEmpty()){
	                	criteria = getEventDao().createDetachedCriteriaForModule(module);
	                }else{// load events by current user id
	                	org.springframework.security.core.userdetails.User _user = 
	                			(org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						String username = _user.getUsername();
						
	                	criteria = getEventDao().createDetachedCriteriaForTarget(username);
	                }
	                	                
	                List<Event> events = getEventDao().getEntityList(criteria, start, limit, sortCriteria, sortOrder);
	                cmd.setTotoalRecords(getEventDao().getRowCount(criteria, Event.class));
	                
	                cmd.addResults("events", events);              
	            }else
            	if(isAction("fire")){
            		Integer id = (Integer)cmd.getParams().get("id");
 	                Event event = getEventById( id );
 	                
 	                AbstractEventHandler eventHandler = this.buildEventHandler(event);
 	               
 	                eventHandler.fire(event);
 	                
            	}
	        }
    	} catch (Exception e) {
    		logger.error(e.getMessage());
            cmd.setError(e.getMessage());    
        }
        
    	return cmd;
	}
	
	private Event getEventById(Integer id) throws Exception {
        return (Event)eventDao.load(id);        
    }
	
    private Event saveEvent(Event event){

        if (event == null) {
            throw new IllegalArgumentException("'event' can not be null");
        }
        event = (Event)eventDao.create(event);
        
        return event; 
    }  
	 
    public void deleteEvent(Event event) throws Exception {
        deleteEvent(event.getId());
    }
    
    public void deleteEvent(Integer id) throws Exception {
        getEventDao().remove(id);        
    }
    
    @Override
	public Event generateEvent(String type, String module, String message,
			String targetRefId) {
		Event event = new Event();
		
		event.setType(type);
		event.setModule(module);
		event.setMessage(message);
		event.setTargetRefId(targetRefId);
		event.setFired(false);
		
		org.springframework.security.core.userdetails.User _user = 
    			(org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		event.setCreatedBy(_user.getUsername());
		
		return event;
	}
    
    private AbstractEventHandler buildEventHandler(Event event){
    	
    	try{
    		String beanName = event.getModule()+Formatter.getInstance().capitalize(event.getType())+"EventHandler";
    		logger.debug("Look up event handler for "+beanName);
    		
    		return (AbstractEventHandler)applicationContext.getBean(beanName);
    	
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("failed to build event handler "+e.getMessage());
    	}
    	
    	return null;
    }

	@Override
	public Event transformRequestToEvent(HttpServletRequest request)
			throws Exception {
		String _id = request.getParameter("id");
		Event event;
		
		try {
			
			if (_id!=null && !_id.trim().isEmpty()) {
				Integer id = Integer.parseInt(_id);
				event = (Event)eventDao.load(id);
				
				event = (Event)this.fillInValueByRequest(event, request);
	        	
	        } else {
	        	event = new Event();
	        	
	        	event = (Event)this.fillInValueByRequest(event, request);
	        	
	        }    
	        
			event = (Event)this.fillInConcurrencyInfo(event, request);   
    				
			//bean validation
	        abstractBeanValidator.validate(IAbstractBeanValidator.VTYPE_EMPTY, "module & targetRefId", event);

		}catch(BeanValidateException bve) {
			throw new SystemServiceException(bve.getMessage());
		}

		return event;
	}

	public IEventDao getEventDao() {
		return eventDao;
	}

	public void setEventDao(IEventDao eventDao) {
		this.eventDao = eventDao;
	}

	public IAbstractBeanValidator getAbstractBeanValidator() {
		return abstractBeanValidator;
	}

	public void setAbstractBeanValidator(
			IAbstractBeanValidator abstractBeanValidator) {
		this.abstractBeanValidator = abstractBeanValidator;
	}

}
