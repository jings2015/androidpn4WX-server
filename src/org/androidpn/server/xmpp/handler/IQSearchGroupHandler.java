package org.androidpn.server.xmpp.handler;

import java.text.SimpleDateFormat;
import java.util.List;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.model.TotalNotification;
import org.androidpn.server.model.UserGroup;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserGroupService;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQSearchGroupHandler extends IQHandler {
	private static final String ELEMENT_NAME = "searchgroup";
	private static final String NAMESPACE = "androidpn:iq:searchgroup";
	private UserGroupService userGroupService;
	public IQSearchGroupHandler() {
		userGroupService = ServiceLocator.getUserGroupService();
	}
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQSearchGroupHandler ");
		IQ reply = null;
		Element searchGroupResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		searchGroupResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String groupId = element.elementText("groupId");
				if(StringUtils.isBlank(groupId)){
					return null;
				}
				UserGroup userGroup = userGroupService.getGroupById(groupId);
				if(userGroup == null){
					searchGroupResult.addElement("count").setText("0");
				}else{
					searchGroupResult.addElement("count").setText("1");
					searchGroupResult.addElement("groupName").setText(userGroup.getGroupName());
					searchGroupResult.addElement("groupId").setText(Long.toString(userGroup.getGroupId()));
					searchGroupResult.addElement("owner").setText(userGroup.getOwner());
					searchGroupResult.addElement("info").setText(userGroup.getInfo());
//					System.out.println("groupName" + userGroup.getGroupName());
//					System.out.println("gorupId" + userGroup.getGroupId());
//					System.out.println("owner" + userGroup.getOwner());
//					System.out.println("info" + userGroup.getInfo());
				}
				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(searchGroupResult);
				System.out.println("reply IQSearchGroupHandler ");
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
