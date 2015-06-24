package org.androidpn.server.xmpp.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.model.TotalNotification;
import org.androidpn.server.model.User;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.TotalNotificationService;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.ErrorCode;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQSendMessage4GroupHandler extends IQHandler {
	private static final String ELEMENT_NAME = "sendmessage4group";
	private static final String NAMESPACE = "androidpn:iq:sendmessage4group";
	private GroupMemberService groupMemberService;
	private TotalNotificationService totalNotificationService;
	private UserService userService;

	public IQSendMessage4GroupHandler() {
		groupMemberService = ServiceLocator.getGroupMemberService();
		totalNotificationService = ServiceLocator.getTotalNotificationService();
		userService = ServiceLocator.getUserService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQSendMessage4GroupHandler ");
		IQ reply = null;
		Element sendMessage4GroupResult = DocumentHelper.createElement(QName
				.get(ELEMENT_NAME, NAMESPACE));
		sendMessage4GroupResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String sender = element.elementText("sender");
				String message = element.elementText("message");
				String groupId = element.elementText("groupId");

				GroupMember groupMember = groupMemberService
						.getGroupMemberByAccountAndGroupId(sender, groupId);
				if (groupMember == null) {
					// 发送者不属于该群
					sendMessage4GroupResult.addElement("ecode").setText(
							ErrorCode.SEND_MESSAGE_4_GROUP_501);
					sendMessage4GroupResult
							.addElement("emsg")
							.setText(
									ErrorCode
											.getEmsg(ErrorCode.SEND_MESSAGE_4_GROUP_501));
				} else {
					// 发送通知
					SessionManager sessionManager = SessionManager
							.getInstance();
					List<GroupMember> groupMembers = groupMemberService
							.getGroupMemberByGroupId(groupId);
					if (groupMembers == null || groupMembers.isEmpty()) {
						// 群内没成员（群不存在）
						sendMessage4GroupResult.addElement("ecode").setText(
								ErrorCode.SEND_MESSAGE_4_GROUP_501);
						sendMessage4GroupResult
								.addElement("emsg")
								.setText(
										ErrorCode
												.getEmsg(ErrorCode.SEND_MESSAGE_4_GROUP_501));
					} else {
						TotalNotification newNotification = new TotalNotification(
								sender, message, groupId);
						// 存消息体，并且获得notificationid
						newNotification = totalNotificationService
								.saveTotalNotification(newNotification);
						String notificationId = Long.toString(newNotification
								.getNotificationId());

						// 组装iq
						Element notification = DocumentHelper
								.createElement(QName.get("notification",
										"androidpn:iq:notification"));
						notification.addElement("notificationId").setText(
								notificationId);
						notification.addElement("title").setText("");
						notification.addElement("message").setText(message);
						notification.addElement("receiver").setText("");
						notification.addElement("sender").setText(sender);
						notification.addElement("groupId").setText(groupId);

						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date cratedDate = newNotification.getCreatedDate();
						String createDateStr = sdf.format(cratedDate);
						notification.addElement("createdDate").setText(
								createDateStr);

						IQ notificationIQ = new IQ();
						notificationIQ.setType(IQ.Type.set);
						notificationIQ.setChildElement(notification);

						for (GroupMember groupMember2 : groupMembers) {
							User user = userService
									.getUserByAccount(groupMember2.getAccount());
							if (user != null) {
//								if (!sender.equals(user.getAccount())) {//发送者不需要推送
									ClientSession clientSession = sessionManager
											.getSession(user.getClientId());
									if (clientSession != null) {
										if (clientSession.getPresence()
												.isAvailable()) {
											notificationIQ.setTo(clientSession
													.getAddress());
											clientSession
													.deliver(notificationIQ);
										}
									}
//								}
							}

						}
						sendMessage4GroupResult.addElement("ecode").setText(
								ErrorCode.SEND_MESSAGE_4_GROUP_SUCCEED);
						sendMessage4GroupResult
								.addElement("emsg")
								.setText(
										ErrorCode
												.getEmsg(ErrorCode.SEND_MESSAGE_4_GROUP_SUCCEED));
					}

				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(sendMessage4GroupResult);
				System.out.println("reply IQSendMessage4GroupHandler ");
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
