package org.androidpn.server.xmpp.handler;

import java.util.List;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQGoupMemberListHandler extends IQHandler {
	private static final String ELEMENT_NAME = "groupmemberlist";
	private static final String NAMESPACE = "androidpn:iq:groupmemberlist";
	private GroupMemberService groupMemberService;
	public IQGoupMemberListHandler() {
		groupMemberService = ServiceLocator.getGroupMemberService();
	}
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQGoupMemberListHandler ");
		IQ reply = null;
		Element groupMemberListResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		groupMemberListResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String groupId = element.elementText("groupId");
				String account = element.elementText("account");
				
				GroupMember groupMember = groupMemberService.getGroupMemberByAccountAndGroupId(account, groupId);
				if(groupMember == null){
					//用户不属于这个群，不给看
					return null;
				}else{
					List<GroupMember> groupMembers = groupMemberService.getGroupMemberByGroupId(groupId);
					if(groupMembers == null || groupMembers.isEmpty()){
						groupMemberListResult.addElement("count").setText("0");
					}else{
						groupMemberListResult.addElement("count").setText(Integer.toString(groupMembers.size()));
						System.out.println("count =" + groupMembers.size());
						int i = 0 ;
						for (GroupMember item : groupMembers) {
							groupMemberListResult.addElement("account" +i).setText(item.getAccount());
							groupMemberListResult.addElement("groupId" +i).setText(Long.toString(item.getGroupId()));
							String pushableStr = item.isPushable()?"1":"0";
							groupMemberListResult.addElement("pushable" +i).setText(pushableStr);
							groupMemberListResult.addElement("owner" +i).setText(item.getOwner());
							i++;
						}
					}
				}
				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(groupMemberListResult);
				System.out.println(reply.toXML());
				System.out.println("reply IQGoupMemberListHandler ");
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
