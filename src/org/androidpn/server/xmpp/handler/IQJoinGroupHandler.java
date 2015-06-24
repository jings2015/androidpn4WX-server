package org.androidpn.server.xmpp.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.model.GroupRequest;
import org.androidpn.server.model.TotalNotification;
import org.androidpn.server.model.User;
import org.androidpn.server.model.UserGroup;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.GroupRequestService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.TotalNotificationService;
import org.androidpn.server.service.UserGroupService;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.ErrorCode;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQJoinGroupHandler extends IQHandler {
	private static final String ELEMENT_NAME = "joingroup";
	private static final String NAMESPACE = "androidpn:iq:joingroup";
	private GroupRequestService groupRequestService;
	private UserGroupService userGroupService;
	private GroupMemberService groupMemberService;
	private TotalNotificationService totalNotificationService;
	private UserService userService;

	public IQJoinGroupHandler() {
		groupRequestService = ServiceLocator.getGroupRequestService();
		userGroupService = ServiceLocator.getUserGroupService();
		groupMemberService = ServiceLocator.getGroupMemberService();
		totalNotificationService = ServiceLocator.getTotalNotificationService();
		userService = ServiceLocator.getUserService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQJoinGroupHandler ");
		IQ reply = null;
		Element joinGroupResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		joinGroupResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String groupId = element.elementText("groupId");
				String account = element.elementText("account");
				String message = element.elementText("message");

				UserGroup userGroup = userGroupService.getGroupById(groupId);

				if (userGroup == null) {
					// 群号不存在
					joinGroupResult.addElement("ecode").setText(
							ErrorCode.JOIN_GROUP_603);
					joinGroupResult.addElement("emsg").setText(
							ErrorCode.getEmsg(ErrorCode.JOIN_GROUP_603));
				} else {
					String groupName = userGroup.getGroupName();
					GroupMember groupMember = groupMemberService
							.getGroupMemberByAccountAndGroupId(account, groupId);
					if (groupMember != null) {
						// 用户已存在群内
						joinGroupResult.addElement("ecode").setText(
								ErrorCode.JOIN_GROUP_601);
						joinGroupResult.addElement("emsg").setText(
								ErrorCode.getEmsg(ErrorCode.JOIN_GROUP_601));
					} else {
						GroupRequest groupRequest = groupRequestService
								.getGroupRequestByAccountAndGroupId(account,
										groupId);
						if (groupRequest != null) {
							// 请求已存在
							joinGroupResult.addElement("ecode").setText(
									ErrorCode.JOIN_GROUP_602);
							joinGroupResult
									.addElement("emsg")
									.setText(
											ErrorCode
													.getEmsg(ErrorCode.JOIN_GROUP_602));
						} else {
							// 保存请求
							GroupRequest newRequest = new GroupRequest();
							newRequest.setGroupId(Long.parseLong(groupId));
							newRequest.setAccount(account);
							newRequest.setMessage(message);
							groupRequestService.saveGroupRequest(newRequest);
							// 返回正确码
							joinGroupResult.addElement("ecode").setText(
									ErrorCode.JOIN_GROUP_SUCCEED);
							joinGroupResult
									.addElement("emsg")
									.setText(
											ErrorCode
													.getEmsg(ErrorCode.JOIN_GROUP_SUCCEED));

							// 发送通知
							String owner = userGroup.getOwner();

							User user = userService.getUserByAccount(owner);

							GroupMember sysGroupMember = groupMemberService
									.getGroupMemberByAccountAndOwner(owner,
											"system");
							if (sysGroupMember != null) {

								String sender = "system";
								String msg = account + "请求加入 " + groupName
										+ "（群号：" + groupId
										+ "),快去请求列表里通过他/她的申请吧！";
								IQ notificationIQ = createNotification(sender,
										msg, Long.toString(sysGroupMember
												.getGroupId()));

								ClientSession clientSession = sessionManager
										.getSession(user.getClientId());
								if (clientSession != null) {
									if (clientSession.getPresence()
											.isAvailable()) {
										notificationIQ.setTo(clientSession
												.getAddress());
										clientSession.deliver(notificationIQ);
									}
								}
							}

						}
					}
				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(joinGroupResult);
				System.out.println("reply IQJoinGroupHandler ");
				session.deliver(reply);
			}
		}
		return null;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	private IQ createNotification(String sender, String message, String groupId) {
		TotalNotification newNotification = new TotalNotification(sender,
				message, groupId);
		// 存消息体，并且获得notificationid
		newNotification = totalNotificationService
				.saveTotalNotification(newNotification);
		String notificationId = Long.toString(newNotification
				.getNotificationId());

		// 组装iq
		Element notification = DocumentHelper.createElement(QName.get(
				"notification", "androidpn:iq:notification"));
		notification.addElement("notificationId").setText(notificationId);
		notification.addElement("title").setText("");
		notification.addElement("message").setText(message);
		notification.addElement("receiver").setText("");
		notification.addElement("sender").setText(sender);
		notification.addElement("groupId").setText(groupId);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date cratedDate = newNotification.getCreatedDate();
		String createDateStr = sdf.format(cratedDate);
		notification.addElement("createdDate").setText(createDateStr);

		IQ notificationIQ = new IQ();
		notificationIQ.setType(IQ.Type.set);
		notificationIQ.setChildElement(notification);
		return notificationIQ;
	}

}
