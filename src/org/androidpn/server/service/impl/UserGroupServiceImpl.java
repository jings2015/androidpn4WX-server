package org.androidpn.server.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.androidpn.server.dao.UserGroupDao;
import org.androidpn.server.model.UserGroup;
import org.androidpn.server.service.UserGroupService;

public class UserGroupServiceImpl implements UserGroupService {
	UserGroupDao userGroupDao ;

	public UserGroup saveGroup(UserGroup group) {
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String flag = sdf.format(date)+random.nextInt(9999);
		group.setFlag(flag);
		userGroupDao.saveGroup(group);
		return userGroupDao.getGroupByFlag(flag);
	}

	public void updateGroup(UserGroup group) {
		userGroupDao.updateGroup(group);
	}

	public UserGroup getGroupById(String id) {
		return userGroupDao.getGroupById(id);
	}

	
	public UserGroupDao getUserGroupDao() {
		return userGroupDao;
	}

	public void setUserGroupDao(UserGroupDao userGroupDao) {
		this.userGroupDao = userGroupDao;
	}

	public String getGroupNameById(String id){
		UserGroup group = getGroupById(id);
		if(group !=null){
			return group.getGroupName();
		}else{
			return null;
		}
	}

	public List<UserGroup> getGroupByOwner(String owenr) {
		return userGroupDao.getGroupByOwner(owenr);
	}
	
	
}
