package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.UserDao;
import org.androidpn.server.model.User;
import org.androidpn.server.service.UserService;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao;

	public void saveUser(User user) {
		if(!userDao.exists(user.getAccount())){
			userDao.saveUser(user);
		}else{
			//TODO 添加的用户名已存在
			
		}

	}

	public void deleteUser(User user) {
		userDao.deleteUser(user);
	}

	public void updateUser(User user) {
		if(userDao.exists(user.getAccount())){
			userDao.updateUser(user);
		}else{
			//TODO 更新的用户不存在
		}

	}

	public User getUserByAccount(String account) {
		return userDao.getUserByAccount(account);
	}
	
	public User getUserByClientId(String clientId){
		return userDao.getUserByClientId(clientId);
	}

	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

	public boolean exists(String account) {
		return userDao.exists(account);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	

}
