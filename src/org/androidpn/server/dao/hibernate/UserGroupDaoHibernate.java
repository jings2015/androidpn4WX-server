package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.UserGroupDao;
import org.androidpn.server.model.UserGroup;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class UserGroupDaoHibernate extends HibernateDaoSupport implements UserGroupDao {

	public void saveGroup(UserGroup userGroup) {
		getHibernateTemplate().save(userGroup);
		getHibernateTemplate().flush();
	}

	public void updateGroup(UserGroup userGroup) {
		getHibernateTemplate().update(userGroup);
	}

	public UserGroup getGroupById(String groupId) {
		Long id = Long.parseLong(groupId);
		return (UserGroup)getHibernateTemplate().get(UserGroup.class, id);
	}
	
	public UserGroup getGroupByFlag(String flag){
		List<UserGroup> list = getHibernateTemplate().find("from UserGroup where flag =?",flag);
		if(list == null || list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	public List<UserGroup> getGroupByOwner(String owenr) {
		return getHibernateTemplate().find("from UserGroup where owner =?",owenr);
	}
	
}
