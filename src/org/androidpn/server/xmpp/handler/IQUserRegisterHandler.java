package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.model.User;
import org.androidpn.server.model.UserGroup;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.ServiceLocator;
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

public class IQUserRegisterHandler extends IQHandler {

	private static final String ELEMENT_NAME = "userregister";
	private static final String NAMESPACE = "androidpn:iq:userregister";
	private UserService userService;
	private GroupMemberService groupMemberService;
	private UserGroupService userGroupService;

	public IQUserRegisterHandler() {
		userService = ServiceLocator.getUserService();
		groupMemberService = ServiceLocator.getGroupMemberService();
		userGroupService = ServiceLocator.getUserGroupService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ reply = null;
		Element registerResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		registerResult.addElement("method").setText(ELEMENT_NAME);
		
		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			// log.error("Session not found for key " + packet.getFrom());
			// reply = IQ.createResultIQ(packet);
			// reply.setChildElement(packet.getChildElement().createCopy());
			// reply.setError(PacketError.Condition.internal_server_error);
			// return reply;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String account = element.elementText("account");
				String password = element.elementText("password");

				if (userService.exists(account)) {
					// 用户名已存在
					registerResult.addElement("ecode").setText(
							ErrorCode.REGISTER_ERROR_201);
					registerResult.addElement("emsg").setText(
							ErrorCode.getEmsg(ErrorCode.REGISTER_ERROR_201));

				} else {
					User user = new User();
					user.setAccount(account);
					user.setPassword(password);
					user.setAlias(account);
//					user.setClientId(clientId);
					userService.saveUser(user);
					
					UserGroup userGroup = new UserGroup();
					userGroup.setGroupName("系统通知");
					userGroup.setOwner("system");
					userGroup = userGroupService.saveGroup(userGroup);
					
					GroupMember groupMember = new GroupMember();
					groupMember.setAccount(account);
					groupMember.setAliasInGroup(account);
					groupMember.setGroupId(userGroup.getGroupId());
					groupMember.setPushable(false);
					groupMember.setOwner("system");
					groupMemberService.saveGroupMember(groupMember);
					
					registerResult.addElement("ecode").setText(
							ErrorCode.REGISTER_SUCCEED);
					registerResult.addElement("emsg").setText(
							ErrorCode.getEmsg(ErrorCode.REGISTER_SUCCEED));

				}
				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(registerResult);
				System.out.println("reply userregister ");
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
