package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.UserGroup;

public interface UserGroupService {
	public UserGroup saveGroup(UserGroup group);
	public void updateGroup(UserGroup group);
	public UserGroup getGroupById(String id);
	public String getGroupNameById(String id);
	public List<UserGroup> getGroupByOwner(String owenr);
}
