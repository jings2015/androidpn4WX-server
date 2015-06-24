package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.xmpp.ErrorCode;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQOutMember4GroupHandler extends IQHandler {
	private static final String ELEMENT_NAME = "outmember4group";
	private static final String NAMESPACE = "androidpn:iq:outmember4group";
	private GroupMemberService groupMemberService;

	public IQOutMember4GroupHandler() {
		groupMemberService = ServiceLocator.getGroupMemberService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQOutMember4GroupHandler ");
		IQ reply = null;
		Element outResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		outResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String groupId = element.elementText("groupId");
				String account = element.elementText("account");
				String owner = element.elementText("owner");
				
				if(account.equals(owner)){
					return null;
				}

				GroupMember groupMember = groupMemberService
						.getGroupMemberByAccountAndGroupId(account, groupId);
				if (groupMember == null) {
					return null;

				} else {
					if (!owner.equals(groupMember.getOwner())) {
						outResult.addElement("ecode").setText(
								ErrorCode.OUT_MEMBER_4_GROUP_901);
						outResult
								.addElement("emsg")
								.setText(
										ErrorCode
												.getEmsg(ErrorCode.OUT_MEMBER_4_GROUP_901));
					}else{
						
						groupMemberService.deleteGroupMember(groupMember);
						outResult.addElement("ecode").setText(
								ErrorCode.OUT_MEMBER_4_GROUP_SUCCEED);
						outResult
								.addElement("emsg")
								.setText(
										ErrorCode
												.getEmsg(ErrorCode.OUT_MEMBER_4_GROUP_SUCCEED));
						
					}
				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(outResult);
				System.out.println("reply IQOutMember4GroupHandler ");
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
