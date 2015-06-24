package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.GroupMemberDao;
import org.androidpn.server.model.GroupMember;
import org.androidpn.server.service.GroupMemberService;

public class GroupMemberServiceImpl implements GroupMemberService {
	GroupMemberDao groupMemberDao;

	public void saveGroupMember(GroupMember groupMember) {
		groupMemberDao.saveGroupMember(groupMember);
	}

	public List<GroupMember> getGroupMemberByAccount(String account) {
		return groupMemberDao.getGroupMemberByAccount(account);
	}

	public List<GroupMember> getGroupMemberByGroupId(String groupId) {
		return groupMemberDao.getGroupMemberByGroupId(groupId);
	}

	public GroupMemberDao getGroupMemberDao() {
		return groupMemberDao;
	}

	public void setGroupMemberDao(GroupMemberDao groupMemberDao) {
		this.groupMemberDao = groupMemberDao;
	}

	public GroupMember getGroupMemberByAccountAndGroupId(String account,
			String groupId) {
		return groupMemberDao.getGroupMemberByAccountAndGroupId(account, groupId);
	}

	public GroupMember getGroupMemberByAccountAndOwner(String account,
			String owner) {
		return groupMemberDao.getGroupMemberByAccountAndOwner(account, owner);
	}

	public void deleteGroupMember(GroupMember groupMember) {
		groupMemberDao.deleteGroupMemeber(groupMember);
	}
	
	

}
