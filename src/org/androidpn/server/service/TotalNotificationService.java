package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.TotalNotification;

public interface TotalNotificationService {
	public TotalNotification saveTotalNotification(TotalNotification totalNotification);
	public void deleteTotalNotification(TotalNotification totalNotification);
	public void deleteTotalNotificationById(String notificationId);
	public TotalNotification getTotalNotificationById(String notificationId);
	public List<TotalNotification> getTotalNotificationByGroupId(String groupId);
	public TotalNotification getLastNotificationByGroupId(String groupId);
	public List<TotalNotification> getTotalNotificationByReceiver(String receiver);
	public List<TotalNotification> getTotalNotificatoinBySender(String sender);
	public List<TotalNotification> getTotalNotificationByReceiverAndGroupId(String receiver,String groupId);
	
}
