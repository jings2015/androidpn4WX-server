//
//  UserApiController.java
//  FeOA
//
//  Created by LuTH on 2012-3-31.
//  Copyright 2012 flyrise. All rights reserved.
//

package org.androidpn.server.console.api;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.ApnUser;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.ApnUserService;
import org.androidpn.server.xmpp.presence.PresenceManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class UserApiController extends MultiActionController {

	private ApnUserService apnUserService;

	public UserApiController() {
		apnUserService = ServiceLocator.getApnUserService();
	}

	public void list(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PresenceManager presenceManager = new PresenceManager();
		List<ApnUser> userList;
		Date createDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			String from = request.getParameter("from").toString();
			if (!"".equals(from)) {
				createDate = sdf.parse(from);
				userList = apnUserService.getApnUsersFromCreatedDate(createDate);
			} else {
				userList = apnUserService.getApnUsers();
			}
		} catch (NullPointerException e) {
			userList = apnUserService.getApnUsers();
		}

		String online = ServletRequestUtils.getStringParameter(request,
				"online", null);

		ArrayList<String> out = new ArrayList<String>();
		for (ApnUser user : userList) {

			if ("1".equals(online) && presenceManager.isAvailable(user)) {
				// Presence presence = presenceManager.getPresence(user);
				user.setOnline(true);
				out.add("\"" + user.getClientId() + "\"");
			} else if ("0".equals(online) && !presenceManager.isAvailable(user)) {
				user.setOnline(false);
				out.add("\"" + user.getClientId() + "\"");
			} else if (online == null) {
				if (presenceManager.isAvailable(user)) {
					// Presence presence = presenceManager.getPresence(user);
					user.setOnline(true);
				} else {
					user.setOnline(false);
				}
				out.add("\"" + user.getClientId() + "\"");
			}
			// logger.debug("user.online=" + user.isOnline());
		}
		response.getWriter().print("{\"data\":" + out.toString() + "}");
	}

}
