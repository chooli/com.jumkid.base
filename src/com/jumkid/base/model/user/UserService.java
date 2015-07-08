package com.jumkid.base.model.user;
/* 
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 * (c)2013 Jumkid All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0        Jan2013      chooli      creation
 * 
 *
 */

import java.util.ConcurrentModificationException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;

import com.jumkid.base.model.usergroup.IUserGroupDao;
import com.jumkid.base.model.usergroup.UserGroup;
import com.jumkid.base.exception.BeanValidateException;
import com.jumkid.base.model.AbstractCommandService;
import com.jumkid.base.model.Command;
import com.jumkid.base.model.IAbstractBeanValidator;
import com.jumkid.base.model.ServiceSession;
import com.jumkid.base.util.Constants;

public class UserService extends AbstractCommandService implements IUserService {
	
	private IUserDao userDao;
	private IUserAuthorityDao userAuthorityDao;
	private IUserGroupDao userGroupDao;
	private IAbstractBeanValidator abstractBeanValidator;
	
	@Override
    public Command execute(Command cmd) throws Exception {
        super.execute(cmd);
        
        try{
        	if (isManager("usermanager")) {
                // load user
                if (isAction("load")) {
                    Integer id = (Integer)cmd.getParams().get("id");
                    String username = (String)cmd.getParams().get("username");
                    
                    User user = null;
                    if(id!=null){
                    	user = (User)userDao.load(id);
                    }else
                    if(username!=null){
                    	user = userDao.loadByUserName(username);
                    }
                    
                    
                    cmd.addResults("user", user);
                
                }else
            	if(isAction("loadByUserName")){
            		String username = (String)cmd.getParams().get("username");
            		User user = userDao.loadByUserName(username);
            		
            		if(user!=null) cmd.addResults("user", user);
            		
            	}else
            	if(isAction("loadByEmail")){
            		String email = (String)cmd.getParams().get("email");
            		User user = userDao.loadByEmail(email);
            		
            		if(user!=null) cmd.addResults("user", user);
            		
            	}else 
                if(isAction("list")) {      
                	String keyword = (String)cmd.getParams().get("keyword");
                	String scope = (String)cmd.getParams().get("scope");
	                Integer start = (Integer)cmd.getParams().get("start");
	                Integer limit = (Integer)cmd.getParams().get("limit");
	                
	                List<User> users = null;
	                if(keyword!=null && !StringUtils.isBlank(keyword)){
	                	DetachedCriteria criteria = userDao.createDetachedCriteria(keyword, scope);
	                	users = userDao.load(criteria, start, limit, null, null);
	                }
                	
                    cmd.addResults("users", users);
                    
                } else if(isAction("savepassword")) {
                	
                    User user = (User)cmd.getParams().get("user");
                    this.getUserDao().update(user);   
                
                } else if(isAction("save")) {
                
                	User user = (User)cmd.getParams().get("user");
                    try {                    	
                        user = saveUser(user);                    
                        cmd.addResults("user", user);
                        
                    } catch (ConcurrentModificationException cme) { 
                        cmd.setError(Constants.CMSG_CONCURRENT_MODIFICATION);
                    } catch (IllegalArgumentException iae) {
                    	cmd.setError("IllegalArgumentException");
                    }                
                } else if(isAction("delete")) {
                	
                    Integer id = (Integer)cmd.getParams().get("id");
                    try {
                        userDao.remove(id);
                    } catch (Exception e) {
                    	logger.error("Failed to remove user "+e.getMessage());
                        cmd.setError(e.getMessage());    
                    }            
                } 
            }
            
            // ============ USERGROUP ============
            if(isManager("usergroupmanager")) {
                // load user
                if(isAction("load")) {
                    Integer id = (Integer)cmd.getParams().get("id");
                    UserGroup userGroup = (UserGroup)userGroupDao.load(id);
                    cmd.addResults("usergroup", userGroup);
                
                } else
                if(isAction("save")) {
                    UserGroup userGroup = (UserGroup)cmd.getParams().get("userGroup");            
                    userGroup = saveUserGroup(userGroup);
                    cmd.addResults("usergroup", userGroup); 
                    
                } else
                if(isAction("delete")) {
                    Integer id = (Integer)cmd.getParams().get("id");
                    try {
                    	userGroupDao.remove(id);
                    } catch (Exception e) {
                    	logger.error("Failed to remove usergroup "+e.getMessage());
                        cmd.setError(e.getMessage());    
                    }            
                } else
                if(isAction("list")) {
                    Integer start = (Integer)cmd.getParams().get("start");
                    Integer limit = (Integer)cmd.getParams().get("limit");
                    List<UserGroup> userGroups = userGroupDao.loadAll(start, limit);
                    cmd.addResults("usergroups", userGroups);
                    
                } 
            }
        } catch(Exception e){
        	logger.error("failed to perform "+cmd.getAction()+" due to "+e.getMessage());
            cmd.setError(e.getLocalizedMessage());
        }
        
        return cmd;
    }

	

	@Override
	public ServiceSession validate(HttpServletRequest request){
		
		ServiceSession sSession = new ServiceSession(request);
		
		String fieldName = request.getParameter("fieldId");
		String value = request.getParameter("fieldValue");
		String vtype = request.getParameter("vtype");
		logger.debug("field: "+fieldName+" value: "+value+" vtype: "+vtype);		
		
		try{
			validate(vtype, fieldName, value);
		}catch(BeanValidateException bve){
			sSession.setErrors(bve.getValidationMessage());
		}
		
		return sSession;
	}
	
	/**
	 * 
	 * @param vtype
	 * @param fieldName
	 * @param value
	 * @throws BeanValidateException
	 */
	public void validate(String vtype, String fieldName, Object value) throws BeanValidateException{
		
		abstractBeanValidator.validate(vtype, fieldName, value, userDao);
		
	}

	@Override
	public User transformRequestToUser(HttpServletRequest request) throws BeanValidateException {
		User user = (User)this.transformRequestToBean(request, User.class, getUserDao());
		    	    
		try{
			//user.setUserAuthorities(userAuthorityDao.loadByUser(user));
			
			//assign role to user if not exist
			if(user.getUserAuthority() == null){
				
				String role = request.getParameter("role");
				UserAuthority userAuthority = new UserAuthority();
				
				if(role!=null && (role.equals(UserAuthority.ROLE_ADMIN) ||
				   role.equals(UserAuthority.ROLE_USER) ||
				   role.equals(UserAuthority.ROLE_AGENT)) ){
					userAuthority.setAuthority(role);
				}else{
					userAuthority.setAuthority(UserAuthority.ROLE_USER);
				}
				userAuthority.setUser(user);
				user.setUserAuthority(userAuthority);
			}
			
			abstractBeanValidator.validate(IAbstractBeanValidator.VTYPE_EMPTY, "username & email", user); 
			
		}catch(BeanValidateException bve) {
			throw new BeanValidateException(bve.getMessage());
		}
    	
    	
    	return user;
    	
	}
	
	@Override
	public UserGroup transformRequestToUserGroup(HttpServletRequest request) throws BeanValidateException{

		UserGroup group = (UserGroup)this.transformRequestToBean(request, UserGroup.class, getUserGroupDao());
        
//        String _parentGroupId = request.getParameter("parentGroupId");
//        if (_parentGroupId!=null) {
//            group.setParentGroupId("".equals(_parentGroupId)?null:Integer.valueOf(_parentGroupId));
//            // TODO check if this works and doesn't set value persistent too early...
//            group.setParentGroup("".equals(_parentGroupId)?null:(UserGroup)userGroupDao.load(Integer.valueOf(_parentGroupId)));
//        }

        //bean validation
    	abstractBeanValidator.validate(IAbstractBeanValidator.VTYPE_EMPTY, UserGroup.PROPERTY_GROUPNAME, group)
    	                     .validate(IAbstractBeanValidator.VTYPE_DUPLICATE, "groupName", group, userGroupDao);
    	
        return group;
    }

	/**
	 * 
	 * @param user
	 * @return
	 * @throws ConcurrentModificationException
	 * @throws IllegalArgumentException
	 */
	private User saveUser(User user) throws ConcurrentModificationException,
			IllegalArgumentException {
		
		if (user == null) {
            throw new IllegalArgumentException("'user' can not be null");
        }

        if (user.getId()==null) {
        	user = (User)userDao.create(user);
        } else {            	        	
        	userDao.update(user);
        	//user = (User)userDao.load(user.getId());
        }

        return user; 
	}
	
	/**
     * @see de.myimmo.web.service.IAgentService#saveUser(de.myimmo.web.entity.Users)
     */
    private UserGroup saveUserGroup(UserGroup userGroup)
    {
        if (userGroup == null)
        {
            throw new IllegalArgumentException("'user' can not be null");
        }
        try
        {
            if (userGroup.getId()==null) {
                userGroup = (UserGroup)userGroupDao.create(userGroup);
            } else {
                getUserGroupDao().update(userGroup);
                userGroup = (UserGroup)userGroupDao.load(userGroup.getId()); 
            }
            return userGroup;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
   
	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IUserGroupDao getUserGroupDao() {
		return userGroupDao;
	}

	public void setUserGroupDao(IUserGroupDao userGroupDao) {
		this.userGroupDao = userGroupDao;
	}


	public IAbstractBeanValidator getAbstractBeanValidator() {
		return abstractBeanValidator;
	}


	public void setAbstractBeanValidator(
			IAbstractBeanValidator abstractBeanValidator) {
		this.abstractBeanValidator = abstractBeanValidator;
	}



	public IUserAuthorityDao getUserAuthorityDao() {
		return userAuthorityDao;
	}



	public void setUserAuthorityDao(IUserAuthorityDao userAuthorityDao) {
		this.userAuthorityDao = userAuthorityDao;
	}


}
