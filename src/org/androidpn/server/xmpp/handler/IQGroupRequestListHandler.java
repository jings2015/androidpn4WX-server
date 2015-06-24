package org.androidpn.server.xmpp.handler;

import java.util.ArrayList;
import java.util.List;

import org.androidpn.server.model.GroupRequest;
import org.androidpn.server.model.UserGroup;
import org.androidpn.server.service.GroupRequestService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserGroupService;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

public class IQGroupRequestListHandler extends IQHandler {
	private static final String ELEMENT_NAME = "grouprequestlist";
	private static final String NAMESPACE = "androidpn:iq:grouprequestlist";
	private GroupRequestService groupRequestService;
	private UserGroupService userGroupService;

	public IQGroupRequestListHandler() {
		groupRequestService = ServiceLocator.getGroupRequestService();
		userGroupService = ServiceLocator.getUserGroupService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQGroupRequestListHandler ");
		IQ reply = null;
		Element groupRequestListResult = DocumentHelper.createElement(QName
				.get(ELEMENT_NAME, NAMESPACE));
		groupRequestListResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String account = element.elementText("account");

				List<UserGroup> userGroups = userGroupService
						.getGroupByOwner(account);
				if (userGroups == null || userGroups.isEmpty()) {
					return null;
				} else {
					List<GroupRequest> groupRequests = new ArrayList<GroupRequest>();
					for (UserGroup userGroup : userGroups) {
						List<GroupRequest> list = groupRequestService
								.getGroupRequestByGroupId(Long
										.toString(userGroup.getGroupId()));
						if (list != null || !list.isEmpty()) {
							for (GroupRequest groupRequest2 : list) {
								groupRequests.add(groupRequest2);
							}
						}
					}
					String count = Integer.toString(groupRequests.size());
					groupRequestListResult.addElement("count").setText(count);
					int i = 0;
					for (GroupRequest groupRequest : groupRequests) {
						groupRequestListResult.addElement("requester" + i)
								.setText(groupRequest.getAccount());
						String groupName = userGroupService
								.getGroupNameById(Long.toString(groupRequest
										.getGroupId()));
						groupRequestListResult.addElement("groupName" + i)
								.setText(groupName);
						groupRequestListResult
								.addElement("groupId" + i)
								.setText(
										Long.toString(groupRequest.getGroupId()));
						groupRequestListResult.addElement("message" + i)
								.setText(groupRequest.getMessage());
						i++;
					}

				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(groupRequestListResult);
				System.out.println("reply IQGroupRequestListHandler ");
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
