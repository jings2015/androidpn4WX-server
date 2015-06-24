package org.androidpn.server.xmpp.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.model.TotalNotification;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.TotalNotificationService;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQNotificationList4Group extends IQHandler {
	private static final String ELEMENT_NAME = "notificationlist4group";
	private static final String NAMESPACE = "androidpn:iq:notificationlist4group";
	private TotalNotificationService totalNotificationService;
	private GroupMemberService groupMemberService;

	public IQNotificationList4Group() {
		totalNotificationService = ServiceLocator.getTotalNotificationService();
		groupMemberService = ServiceLocator.getGroupMemberService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQNotificationList4Group ");
		IQ reply = null;
		Element notificationList4GroupResult = DocumentHelper
				.createElement(QName.get(ELEMENT_NAME, NAMESPACE));
		notificationList4GroupResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String account = element.elementText("account");
				String groupId = element.elementText("groupId");
				String count = element.elementText("count");
				if (StringUtils.isBlank(account)
						|| StringUtils.isBlank(groupId)
						|| StringUtils.isBlank(count)) {
					return null;
				}
				GroupMember groupMember = groupMemberService
						.getGroupMemberByAccountAndGroupId(account, groupId);
				if (groupMember == null) {
					return null;
				}

				// 获取客户端发送上来的notificationId列表
				List<String> notificationIds = new ArrayList<String>();
				for (int i = 0; i < Integer.parseInt(count); i++) {
					String notificationId = element
							.elementText("notificationId" + i);
					notificationIds.add(notificationId);
				}
				System.out.println("notificationIds.size() = "
						+ notificationIds.size());
				// 根据groupId从数据库查询消息记录
				List<TotalNotification> totalNotifications = totalNotificationService
						.getTotalNotificationByGroupId(groupId);

				// 记录客户端已有的消息
				List<Integer> removeList = new ArrayList<Integer>();
				for (int i = 0; i < totalNotifications.size(); i++) {
					TotalNotification totalNotification = totalNotifications
							.get(i);
					for (int j = 0; j < notificationIds.size(); j++) {
						String idStr = Long.toString(totalNotification
								.getNotificationId());
						if (idStr.equals(notificationIds.get(j))) {
							removeList.add(i);
							notificationIds.remove(j);
							break;
						}
					}
				}

				System.out.println("removeList.size() = " + removeList.size());
				// 去除客户端重复消息
				for (int i = removeList.size() - 1; i >= 0; i--) {
					System.out.println("remove index" + removeList.get(i));
					totalNotifications.remove((int)removeList.get(i));
				}
				System.out.println("totalNotifications.size() " + totalNotifications.size());
				notificationList4GroupResult.addElement("count").setText(
						Integer.toString(totalNotifications.size()));
				for (int i = 0; i < totalNotifications.size(); i++) {
					TotalNotification totalNotification = totalNotifications
							.get(i);
					notificationList4GroupResult.addElement("sender" + i)
							.setText(totalNotification.getSender());
					notificationList4GroupResult.addElement("receiver" + i)
							.setText(totalNotification.getReceiver());
					notificationList4GroupResult.addElement(
							"notificationId" + i)
							.setText(
									Long.toString(totalNotification
											.getNotificationId()));
					notificationList4GroupResult.addElement("message" + i)
							.setText(totalNotification.getMessage());
					notificationList4GroupResult.addElement("groupId" + i)
							.setText(totalNotification.getGroupId());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					String createdDate = sdf.format(totalNotification
							.getCreatedDate());
					notificationList4GroupResult.addElement("createdDate" + i)
							.setText(createdDate);
				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(notificationList4GroupResult);
				System.out.println("reply IQNotificationList4Group ");
				System.out.println(reply.toXML());
				session.deliver(reply);
			}
		}
		return null;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

}
