package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.UserGroup;

public interface UserGroupDao {
	public void saveGroup(UserGroup group);
	public void updateGroup(UserGroup group);
	public UserGroup getGroupById(String id);
	public UserGroup getGroupByFlag(String flag);
	public List<UserGroup> getGroupByOwner(String owenr);
}
