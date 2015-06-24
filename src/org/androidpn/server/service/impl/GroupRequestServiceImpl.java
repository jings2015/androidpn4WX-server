package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.GroupRequestDao;
import org.androidpn.server.model.GroupRequest;
import org.androidpn.server.service.GroupRequestService;

public class GroupRequestServiceImpl implements GroupRequestService {
	GroupRequestDao groupRequestDao;
	

	public void saveGroupRequest(GroupRequest groupRequest) {
		groupRequestDao.saveGroupRequest(groupRequest);
	}

	public void deleteGroupRequest(GroupRequest groupRequest) {
		groupRequestDao.deleteGroupRequest(groupRequest);
	}

	public List<GroupRequest> getGroupRequestByGroupId(String groupId) {
		return groupRequestDao.getGroupRequestByGroupId(groupId);
	}

	public GroupRequest getGroupRequestByAccountAndGroupId(String account,
			String groupId) {
		return groupRequestDao.getGroupRequestByAccountAndGroupId(account, groupId);
	}

	public GroupRequestDao getGroupRequestDao() {
		return groupRequestDao;
	}

	public void setGroupRequestDao(GroupRequestDao groupRequestDao) {
		this.groupRequestDao = groupRequestDao;
	}

	
}
