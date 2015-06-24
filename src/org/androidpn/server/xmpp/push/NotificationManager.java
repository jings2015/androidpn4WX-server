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
package org.androidpn.server.xmpp.push;

import java.util.Random;

import org.androidpn.server.model.TotalNotification;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.TotalNotificationService;
import org.androidpn.server.service.impl.TotalNotificationServiceImpl;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

/**
 * This class is to manage sending the notifcations to the users.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationManager {

	private static final String NOTIFICATION_NAMESPACE = "androidpn:iq:notification";

	private final Log log = LogFactory.getLog(getClass());

	private SessionManager sessionManager;

	private TotalNotificationService totalNotificationService;

	/**
	 * Constructor.
	 */
	public NotificationManager() {
		sessionManager = SessionManager.getInstance();
		totalNotificationService = ServiceLocator.getTotalNotificationService();
	}

	/**
	 * Broadcasts a newly created notification message to all connected users.
	 * 
	 * @param apiKey
	 *            the API key
	 * @param title
	 *            the title
	 * @param message
	 *            the message details
	 * @param uri
	 *            the uri
	 */
	public void sendBroadcast(String title, String message, String groupId,
			String receiver, String sender) {
		log.debug("sendBroadcast()...");
		IQ notificationIQ = createNotificationIQ(title, message, groupId,
				receiver, sender);
		for (ClientSession session : sessionManager.getSessions()) {
			if (session.getPresence().isAvailable()) {
				notificationIQ.setTo(session.getAddress());
				session.deliver(notificationIQ);
			}
		}
	}

	/**
	 * Sends a newly created notification message to the specific user.
	 * 
	 * @param apiKey
	 *            the API key
	 * @param title
	 *            the title
	 * @param message
	 *            the message details
	 * @param uri
	 *            the uri
	 */
	public void sendNotifcationToUser(String clientId, String title,
			String message, String groupId, String receiver, String sender) {
		log.debug("sendNotifcationToUser()...");
		IQ notificationIQ = createNotificationIQ(title, message, groupId,
				receiver, sender);
		ClientSession session = sessionManager.getSession(clientId);
		if (session != null) {
			if (session.getPresence().isAvailable()) {
				notificationIQ.setTo(session.getAddress());
				session.deliver(notificationIQ);
			}
		}
	}

	/**
	 * Creates a new notification IQ and returns it.
	 */
	private IQ createNotificationIQ(String title, String message,
			String groupId, String receiver, String sender) {
		// Random random = new Random();
		// String id = Integer.toHexString(random.nextInt());
		// String id = String.valueOf(System.currentTimeMillis());

		TotalNotification newNotification = new TotalNotification(receiver,
				sender, groupId, title, message);
		// 存消息体，并且获得notificationid
		newNotification = totalNotificationService
				.saveTotalNotification(newNotification);
		String notificationId = Long.toString(newNotification
				.getNotificationId());

		// 组装iq
		Element notification = DocumentHelper.createElement(QName.get(
				"notification", NOTIFICATION_NAMESPACE));
		notification.addElement("notificationId").setText(notificationId);
		notification.addElement("title").setText(title);
		notification.addElement("message").setText(message);
		notification.addElement("receiver").setText(receiver);
		notification.addElement("sender").setText(sender);
		notification.addElement("groupId").setText(groupId);

		IQ iq = new IQ();
		iq.setType(IQ.Type.set);
		iq.setChildElement(notification);

		return iq;
	}
}
