package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.UserGroup;
import org.androidpn.server.model.GroupMember;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.UserGroupService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.xmpp.ErrorCode;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQCreateGroupHandler extends IQHandler {
	private static final String ELEMENT_NAME = "creategroup";
	private static final String NAMESPACE = "androidpn:iq:creategroup";

	private UserGroupService userGroupService;
	private GroupMemberService groupMemberService;

	public IQCreateGroupHandler() {
		userGroupService = ServiceLocator.getUserGroupService();
		groupMemberService = ServiceLocator.getGroupMemberService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQCreateGroup ");
		IQ reply = null;
		Element createGroupResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		createGroupResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String account = element.elementText("account");
				String groupName = element.elementText("groupName");
				String info = element.elementText("info");

				UserGroup newGroup;
				UserGroup group = new UserGroup();
				group.setOwner(account);
				group.setGroupName(groupName);
				group.setInfo(info);
				newGroup = userGroupService.saveGroup(group);
				if (newGroup != null) {
					GroupMember groupMember = new GroupMember();
					groupMember.setAccount(account);
					groupMember.setAliasInGroup(account);
					groupMember.setGroupId(newGroup.getGroupId());
					groupMember.setPushable(true);
					groupMember.setOwner(account);
					groupMemberService.saveGroupMember(groupMember);

					createGroupResult.addElement("ecode").setText(
							ErrorCode.CREATE_GROUP_SUCCEED);
					createGroupResult.addElement("emsg").setText(
							ErrorCode.getEmsg(ErrorCode.CREATE_GROUP_SUCCEED));
				} else {
					createGroupResult.addElement("ecode").setText(
							ErrorCode.CREATE_GROUP_ERROR_401);
					createGroupResult
							.addElement("emsg")
							.setText(
									ErrorCode
											.getEmsg(ErrorCode.CREATE_GROUP_ERROR_401));
				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(createGroupResult);
				System.out.println("reply IQCreateGroup ");
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
