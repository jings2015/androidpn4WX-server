package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.GroupMember;

public interface GroupMemberService {
	public void saveGroupMember(GroupMember groupMember);
	public List<GroupMember> getGroupMemberByAccount(String account);
	public List<GroupMember> getGroupMemberByGroupId(String groupId);
	public GroupMember getGroupMemberByAccountAndGroupId(String account,String groupId);
	public GroupMember getGroupMemberByAccountAndOwner(String account,String owner);
	public void deleteGroupMember(GroupMember groupMember);
}
