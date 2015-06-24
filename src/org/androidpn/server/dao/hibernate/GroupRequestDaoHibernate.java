package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.GroupRequestDao;
import org.androidpn.server.model.GroupRequest;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class GroupRequestDaoHibernate extends HibernateDaoSupport implements
		GroupRequestDao {

	public void saveGroupRequest(GroupRequest groupRequest) {
		getHibernateTemplate().save(groupRequest);
		getHibernateTemplate().flush();
	}

	public void deleteGroupRequest(GroupRequest groupRequest) {
		getHibernateTemplate().delete(groupRequest);
	}

	public List<GroupRequest> getGroupRequestByGroupId(String groupId) {
		String hql = "from GroupRequest g where g.groupId =" + groupId;
		List<GroupRequest> list = getHibernateTemplate().find(hql);
		return list;
	}

	public GroupRequest getGroupRequestByAccountAndGroupId(String account,
			String groupId) {
		String hql = "from GroupRequest g where g.account = '" + account
				+ "' and g.groupId =" + groupId;
		List<GroupRequest> list = getHibernateTemplate().find(hql);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

}
