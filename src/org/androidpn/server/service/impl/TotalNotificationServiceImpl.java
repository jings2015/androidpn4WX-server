package org.androidpn.server.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.androidpn.server.dao.TotalNotificationDao;
import org.androidpn.server.model.TotalNotification;
import org.androidpn.server.service.TotalNotificationService;

public class TotalNotificationServiceImpl implements TotalNotificationService {
	
	private TotalNotificationDao totalNotificationDao;
	

	public TotalNotification saveTotalNotification(TotalNotification totalNotification) {
		//按照"yyyyMMddHHmmss"+4为随机数 的规则生成notificationId
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		Random random = new Random();
		String notificationId = sdf.format(date)+random.nextInt(9999);
		if(totalNotificationDao == null){
			System.out.println("totalnotificationDao is null");
		}else{
			System.out.println("totalnotificationDao is not null");
		}
		if(totalNotification == null){
			System.out.println("totalnotification is null");
		}else{
			System.out.println("totalnotification is not null");
		}
		TotalNotification has = getTotalNotificationById(notificationId);
		if(has == null){
			totalNotification.setNotificationId(Long.parseLong(notificationId));
			totalNotificationDao.saveTotalNotification(totalNotification);
			return totalNotification;
		}else{
			return saveTotalNotification(totalNotification);
		}
	}

	public void deleteTotalNotification(TotalNotification totalNotification) {
		totalNotificationDao.deleteTotalNotification(totalNotification);
	}

	public void deleteTotalNotificationById(String notificationId){
		TotalNotification totalNotification = getTotalNotificationById(notificationId);
		if(totalNotification != null){
			deleteTotalNotification(totalNotification);
		}
	}
	
	@SuppressWarnings("finally")
	public TotalNotification getTotalNotificationById(String notificationId) {
		TotalNotification totalNotification = null;
		try{
			totalNotification = totalNotificationDao.getTotalNotificationById(notificationId);
			
		}catch (NullPointerException e) {
			System.out.println("消息" + notificationId + "：不存在！ ");
			totalNotification = null;
		}finally{
			return totalNotification;
		}
	}

	public List<TotalNotification> getTotalNotificationByGroupId(String groupId) {
		return totalNotificationDao.getTotalNotificationByGroupId(groupId);
	}

	public List<TotalNotification> getTotalNotificationByReceiver(
			String receiver) {
		return totalNotificationDao.getTotalNotificationByReceiver(receiver);
	}

	public List<TotalNotification> getTotalNotificatoinBySender(String sender) {
		return totalNotificationDao.getTotalNotificatoinBySender(sender);
	}

	public TotalNotificationDao getTotalNotificationDao() {
		return totalNotificationDao;
	}

	public void setTotalNotificationDao(TotalNotificationDao totalNotificationDao) {
		this.totalNotificationDao = totalNotificationDao;
	}

	public TotalNotification getLastNotificationByGroupId(String groupId) {
		List<TotalNotification> list = getTotalNotificationByGroupId(groupId);
		if(list == null || list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	public List<TotalNotification> getTotalNotificationByReceiverAndGroupId(
			String receiver, String groupId) {
		return totalNotificationDao.getTotalNotificationByReceiverAndGroupId(receiver, groupId);
	}
	


	
}
