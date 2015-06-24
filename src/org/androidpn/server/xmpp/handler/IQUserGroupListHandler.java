package org.androidpn.server.xmpp.handler;

import java.text.SimpleDateFormat;
import java.util.List;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.model.TotalNotification;
import org.androidpn.server.model.UserGroup;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.TotalNotificationService;
import org.androidpn.server.service.UserGroupService;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQUserGroupListHandler extends IQHandler {
	private static final String ELEMENT_NAME = "usergrouplist";
	private static final String NAMESPACE = "androidpn:iq:usergrouplist";

	GroupMemberService groupMemberService;
	UserGroupService userGroupService;
	TotalNotificationService totalNotificationService;

	public IQUserGroupListHandler() {
		groupMemberService = ServiceLocator.getGroupMemberService();
		userGroupService = ServiceLocator.getUserGroupService();
		totalNotificationService = ServiceLocator.getTotalNotificationService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQUserGroupListHandler ");
		IQ reply = null;
		Element userGroupResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		userGroupResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String account = element.elementText("account");
				if(StringUtils.isBlank(account)){
					return null;
				}
				List<GroupMember> list = groupMemberService
						.getGroupMemberByAccount(account);
				if (list == null || list.isEmpty()) {
					userGroupResult.addElement("count").setText("0");
				} else {
					int count = list.size();
					System.out.println("count =" + count);
					userGroupResult.addElement("count").setText(
							Integer.toString(count));
					int i = 0;
					// 所有属性后缀+i,i从0到count-1
					for (GroupMember groupMember : list) {
						String groupId = Long
								.toString(groupMember.getGroupId());
						
						UserGroup userGroup = userGroupService.getGroupById(groupId);
						
						String groupName = userGroup.getGroupName();
						String info = userGroup.getInfo();
						if(info == null){
							info = "";
						}
						String owner = userGroup.getOwner();
						String pushable = groupMember.isPushable()?"1":"0";
						TotalNotification notification = totalNotificationService
								.getLastNotificationByGroupId(groupId);
						String message;
						String createdDate;
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm");
						if (notification == null) {
							message = "";
							createdDate = "";
						} else {
							message = notification.getMessage();
							createdDate = sdf.format(notification
									.getCreatedDate());
						}
						userGroupResult.addElement("groupId" + i).setText(
								groupId);
						userGroupResult.addElement("groupName" + i).setText(
								groupName);
						userGroupResult.addElement("info" + i).setText(info);
						userGroupResult.addElement("owner" + i).setText(owner);
						userGroupResult.addElement("pushable" + i).setText(pushable);
						userGroupResult.addElement("message" + i).setText(
								message);
						userGroupResult.addElement("createdDate" + i).setText(
								createdDate);
						i++;
					}
				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(userGroupResult);
				System.out.println("reply IQUserGroupListHandler ");
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
