package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.UserDao;
import org.androidpn.server.model.User;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class UserDaoHibernate extends HibernateDaoSupport implements UserDao {

	public void saveUser(User user) {
		getHibernateTemplate().saveOrUpdate(user);
		getHibernateTemplate().flush();
	}

	public void deleteUser(User user) {
		getHibernateTemplate().delete(user);
	}

	public void updateUser(User user) {
		getHibernateTemplate().update(user);
	}

	public User getUserByAccount(String account) {
		return (User) getHibernateTemplate().get(User.class, account);
	}

	public User getUserByClientId(String clientId) {
		List<User> list = getHibernateTemplate().find(
				"from User where clientId =?", clientId);
		if(list == null || list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		return getHibernateTemplate().find(
				"from User u order by u.createdDate desc");
	}

	public boolean exists(String account) {
		User user = (User) getHibernateTemplate().get(User.class, account);
		return user != null;
	}

}
