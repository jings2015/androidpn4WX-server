package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.GroupMember;
import org.androidpn.server.model.GroupRequest;
import org.androidpn.server.model.User;
import org.androidpn.server.model.UserGroup;
import org.androidpn.server.service.GroupMemberService;
import org.androidpn.server.service.GroupRequestService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserGroupService;
import org.androidpn.server.xmpp.ErrorCode;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQHandleGroupRequestHandler extends IQHandler {
	private static final String ELEMENT_NAME = "handlegrouprequest";
	private static final String NAMESPACE = "androidpn:iq:handlegrouprequest";
	private GroupRequestService groupRequestService;
	private GroupMemberService groupMemberService;
	private UserGroupService userGroupService;

	public IQHandleGroupRequestHandler() {
		groupRequestService = ServiceLocator.getGroupRequestService();
		groupMemberService = ServiceLocator.getGroupMemberService();
		userGroupService = ServiceLocator.getUserGroupService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQJoinGroupHandler ");
		IQ reply = null;
		Element handleGroupRequestResult = DocumentHelper.createElement(QName
				.get(ELEMENT_NAME, NAMESPACE));
		handleGroupRequestResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String requester = element.elementText("requester");
				String groupId = element.elementText("groupId");
				String result = element.elementText("result");
				if (!"agree".equals(result) && !"disagree".equals(result)) {
					handleGroupRequestResult.addElement("ecode").setText(
							ErrorCode.HANDLE_GROUP_REQUEST_702);
					handleGroupRequestResult
							.addElement("emsg")
							.setText(
									ErrorCode
											.getEmsg(ErrorCode.HANDLE_GROUP_REQUEST_702));
				} else {
					GroupRequest groupRequest = groupRequestService
							.getGroupRequestByAccountAndGroupId(requester,
									groupId);
					UserGroup userGroup = userGroupService
							.getGroupById(groupId);
					if (groupRequest == null || userGroup == null) {
						handleGroupRequestResult.addElement("ecode").setText(
								ErrorCode.HANDLE_GROUP_REQUEST_702);
						handleGroupRequestResult
								.addElement("emsg")
								.setText(
										ErrorCode
												.getEmsg(ErrorCode.HANDLE_GROUP_REQUEST_702));
					} else {
						// 处理操作
						String owner = userGroup.getOwner();
						if ("agree".equals(result)) {
							// 通过
							GroupMember groupMember = new GroupMember();
							groupMember.setAccount(requester);
							groupMember.setGroupId(Long.parseLong(groupId));
							groupMember.setOwner(owner);
							groupMember.setPushable(false);
							groupMemberService.saveGroupMember(groupMember);

							handleGroupRequestResult
									.addElement("ecode")
									.setText(
											ErrorCode.HANDLE_GROUP_REQUEST_AGREE);
							handleGroupRequestResult
									.addElement("emsg")
									.setText(
											ErrorCode
													.getEmsg(ErrorCode.HANDLE_GROUP_REQUEST_AGREE));
						} else {
							handleGroupRequestResult
									.addElement("ecode")
									.setText(
											ErrorCode.HANDLE_GROUP_REQUEST_DISAGREE);
							handleGroupRequestResult
									.addElement("emsg")
									.setText(
											ErrorCode
													.getEmsg(ErrorCode.HANDLE_GROUP_REQUEST_DISAGREE));
						}
						groupRequestService.deleteGroupRequest(groupRequest);

					}

				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(handleGroupRequestResult);
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

}
