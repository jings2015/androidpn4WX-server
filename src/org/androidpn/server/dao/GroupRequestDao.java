package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.GroupRequest;

public interface GroupRequestDao {
	public void saveGroupRequest(GroupRequest groupRequest);
	public void deleteGroupRequest(GroupRequest groupRequest);
	public List<GroupRequest> getGroupRequestByGroupId(String groupId);
	public GroupRequest getGroupRequestByAccountAndGroupId(String account,String groupId);
}
