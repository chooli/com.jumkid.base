package com.jumkid.base.model.event;

import com.jumkid.base.model.user.IUserDao;
import com.jumkid.base.model.user.User;

public class FriendConnectEventHandler extends AbstractEventHandler{

	private IUserDao userDao;
	
	@Override
	public void fire(Event event) throws Exception {
		
		//add as friend each other
		String ownerId = event.getCreatedBy();
		User owner = userDao.loadByUserName(ownerId);
		
		String targetUserId = event.getTargetRefId();
		User targetUser = userDao.loadByUserName(targetUserId);
		
		owner.addFriend(targetUser);
		targetUser.addFriend(owner);
		
		userDao.update(owner);
		//userDao.update(targetUser);
		
		//update event as fired
		event.setFired(true);
		this.getEventDao().update(event);
		
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

}
