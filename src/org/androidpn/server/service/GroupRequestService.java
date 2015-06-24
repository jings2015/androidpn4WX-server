package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.GroupRequest;

public interface GroupRequestService {
	public void saveGroupRequest(GroupRequest groupRequest);
	public void deleteGroupRequest(GroupRequest groupRequest);
	public List<GroupRequest> getGroupRequestByGroupId(String groupId);
	public GroupRequest getGroupRequestByAccountAndGroupId(String account,String groupId);
}
