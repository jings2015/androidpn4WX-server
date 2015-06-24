package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.TotalNotification;

public interface TotalNotificationDao {
	public void saveTotalNotification(TotalNotification totalNotification);
	public void deleteTotalNotification(TotalNotification totalNotification);
	public TotalNotification getTotalNotificationById(String notificationId);
	public List<TotalNotification> getTotalNotificationByGroupId(String groupId);
	public List<TotalNotification> getTotalNotificationByReceiver(String receiver);
	public List<TotalNotification> getTotalNotificatoinBySender(String sender);
	public List<TotalNotification> getTotalNotificationByReceiverAndGroupId(String receiver,String groupId);
}
