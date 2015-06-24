package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.GroupMemberDao;
import org.androidpn.server.model.GroupMember;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class GroupMemberDaoHibernate extends HibernateDaoSupport implements
		GroupMemberDao {

	public void saveGroupMember(GroupMember groupMember) {
		getHibernateTemplate().save(groupMember);
		getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	public List<GroupMember> getGroupMemberByAccount(String account) {
		List<GroupMember> list = getHibernateTemplate().find(
				"from GroupMember where account = ?", account);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<GroupMember> getGroupMemberByGroupId(String groupId) {
		List<GroupMember> list = getHibernateTemplate().find(
				"from GroupMember where groupId = "+ groupId);
		return list;
	}

	@SuppressWarnings("unchecked")
	public GroupMember getGroupMemberByAccountAndGroupId(String account,
			String groupId) {
		String[] str = new String[2];
		str[0] = account;
		str[1] = groupId;

		String hql = "from GroupMember g where g.account = '" + account
				+ "' and g.groupId = " + groupId;
		List<GroupMember> list = getHibernateTemplate().find(hql);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public GroupMember getGroupMemberByAccountAndOwner(String account,
			String owner) {
		String[] str = new String[2];
		str[0] = account;
		str[1] = owner;
		List<GroupMember> list = getHibernateTemplate().find("from GroupMember g where g.account = ? and g.owner = ?",str);
		if(list == null || list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	public void deleteGroupMemeber(GroupMember groupMember) {
		getHibernateTemplate().delete(groupMember);
	}
	
	

}
