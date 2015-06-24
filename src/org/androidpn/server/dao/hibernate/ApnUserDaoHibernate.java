/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.androidpn.server.dao.ApnUserDao;
import org.androidpn.server.model.ApnUser;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class is the implementation of UserDAO using Spring's HibernateTemplate.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ApnUserDaoHibernate extends HibernateDaoSupport implements ApnUserDao {

	public ApnUser getApnUser(Long id) {
		return (ApnUser) getHibernateTemplate().get(ApnUser.class, id);
	}

	public ApnUser saveApnUser(ApnUser apnUser) {
		getHibernateTemplate().save(apnUser);
		getHibernateTemplate().flush();
		return apnUser;
	}
	
	public void updateApnUser(ApnUser apnUser){
		getHibernateTemplate().update(apnUser);
	}

	public void removeApnUser(Long id) {
		getHibernateTemplate().delete(getApnUser(id));
	}

	public boolean exists(Long id) {
		ApnUser user = (ApnUser) getHibernateTemplate().get(ApnUser.class, id);
		return user != null;
	}

	@SuppressWarnings("unchecked")
	public List<ApnUser> getApnUsers() {
		return getHibernateTemplate().find(
				"from ApnUser u order by u.createdDate desc");
	}

	@SuppressWarnings("unchecked")
	public List<ApnUser> getApnUsersFromCreatedDate(Date createDate) {
		return getHibernateTemplate()
				.find("from ApnUser u where u.createdDate >= ? order by u.createdDate desc",
						createDate);
	}

	@SuppressWarnings("unchecked")
	public ApnUser getApnUserByClientId(String clientId) {
		List<ApnUser> users = getHibernateTemplate().find("from ApnUser where clientId=?",
				clientId);
		if (users == null || users.isEmpty()) {
			return null;
		} else {
			return (ApnUser) users.get(0);
		}
	}

	// @SuppressWarnings("unchecked")
	// public User findUserByUsername(String username) {
	// List users = getHibernateTemplate().find("from User where username=?",
	// username);
	// return (users == null || users.isEmpty()) ? null : (User) users.get(0);
	// }

}
