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

public class IQChangePushable4GroupHandler extends IQHandler {
	private static final String ELEMENT_NAME = "changepushable4group";
	private static final String NAMESPACE = "androidpn:iq:changepushable4group";
	private GroupMemberService groupMemberService;

	public IQChangePushable4GroupHandler() {
		groupMemberService = ServiceLocator.getGroupMemberService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQChangePushable4GroupHandler ");
		IQ reply = null;
		Element changeResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		changeResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String groupId = element.elementText("groupId");
				String account = element.elementText("account");
				String result = element.elementText("result");
				String owner = element.elementText("owner");
				if(account.equals(owner)){
					return null;
				}

				if (!"1".equals(result) && !"0".equals(result)) {
					return null;
				} else {
					GroupMember groupMember = groupMemberService
							.getGroupMemberByAccountAndGroupId(account, groupId);
					if (groupMember == null) {
						return null;
					} else {
						if (!owner.equals(groupMember.getOwner())) {
							changeResult.addElement("ecode").setText(
									ErrorCode.CHANGE_PUSHABLE_4_GROUP_805);
							changeResult
									.addElement("emsg")
									.setText(
											ErrorCode
													.getEmsg(ErrorCode.CHANGE_PUSHABLE_4_GROUP_805));
						}else{
							boolean resultBool = "1".equals(result)?true:false;
							if(resultBool){
								//开启权限
								if(groupMember.isPushable()){
									//用户已有发送权限，无需重复开启
									changeResult.addElement("ecode").setText(
											ErrorCode.CHANGE_PUSHABLE_4_GROUP_802);
									changeResult
											.addElement("emsg")
											.setText(
													ErrorCode
															.getEmsg(ErrorCode.CHANGE_PUSHABLE_4_GROUP_802));
								}else{
									//开
									groupMemberService.deleteGroupMember(groupMember);
									groupMember.setPushable(resultBool);
									groupMemberService.saveGroupMember(groupMember);
									
									changeResult.addElement("ecode").setText(
											ErrorCode.CHANGE_PUSHABLE_4_GROUP_800);
									changeResult
											.addElement("emsg")
											.setText(
													ErrorCode
															.getEmsg(ErrorCode.CHANGE_PUSHABLE_4_GROUP_800));
								}
							}else{
								//关闭权限
								if(!groupMember.isPushable()){
									//用户没有发送权限，无需重复关闭
									changeResult.addElement("ecode").setText(
											ErrorCode.CHANGE_PUSHABLE_4_GROUP_803);
									changeResult
											.addElement("emsg")
											.setText(
													ErrorCode
															.getEmsg(ErrorCode.CHANGE_PUSHABLE_4_GROUP_803));
								}else{
									//关
									groupMemberService.deleteGroupMember(groupMember);
									groupMember.setPushable(resultBool);
									groupMemberService.saveGroupMember(groupMember);
									changeResult.addElement("ecode").setText(
											ErrorCode.CHANGE_PUSHABLE_4_GROUP_801);
									changeResult
											.addElement("emsg")
											.setText(
													ErrorCode
															.getEmsg(ErrorCode.CHANGE_PUSHABLE_4_GROUP_801));
								}
							}
							
						}
					}
				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(changeResult);
				System.out.println("reply IQChangePushable4GroupHandler ");
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
