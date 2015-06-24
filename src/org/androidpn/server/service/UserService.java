package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.User;

public interface UserService {
	public void saveUser(User user);
	public void deleteUser(User user);
	public void updateUser(User user);
	public User getUserByAccount(String account);
	public User getUserByClientId(String clientId);
	public List<User> getAllUsers();
	public boolean exists(String account);
}
