package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.TotalNotificationDao;
import org.androidpn.server.model.TotalNotification;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TotalNotificationDaoHibernate extends HibernateDaoSupport
		implements TotalNotificationDao {

	public void saveTotalNotification(TotalNotification totalNotification) {
		getHibernateTemplate().save(totalNotification);
		getHibernateTemplate().flush();
	}

	public void deleteTotalNotification(TotalNotification totalNotification) {
		getHibernateTemplate().delete(totalNotification);
	}

	@SuppressWarnings("unchecked")
	public TotalNotification getTotalNotificationById(String notificationId) {
		List<TotalNotification> list = getHibernateTemplate().find(
				"from TotalNotification t where t.notificationId=?",
				notificationId);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public List<TotalNotification> getTotalNotificationByGroupId(String groupId) {
		return getHibernateTemplate()
				.find("from TotalNotification t where t.groupId=? order by t.createdDate desc",
						groupId);
	}

	@SuppressWarnings("unchecked")
	public List<TotalNotification> getTotalNotificationByReceiver(
			String receiver) {
		return getHibernateTemplate()
				.find("from TotalNotification t where t.receiver=? order by t.createdDate desc",
						receiver);
	}

	@SuppressWarnings("unchecked")
	public List<TotalNotification> getTotalNotificatoinBySender(String sender) {
		return getHibernateTemplate()
				.find("from TotalNotification t where t.sender=? order by t.createdDate desc",
						sender);
	}

	public List<TotalNotification> getTotalNotificationByReceiverAndGroupId(
			String receiver, String groupId) {
		String[] str = new String[2];
		str[0] = receiver;
		str[1] = groupId;
		@SuppressWarnings("unchecked")
		List<TotalNotification> list = getHibernateTemplate()
				.find("from TotalNotification t where t.receiver = ? and t.groupId = ?",
						str);
		return list;
	}

}
