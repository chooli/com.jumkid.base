package com.jumkid.base.model.usergroup;

import java.util.List;

import com.jumkid.base.model.ICommonBeanDao;

public interface IUserGroupDao extends ICommonBeanDao{
    
    public List<UserGroup> loadRootGroups();
    
}
