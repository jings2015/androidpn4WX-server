/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.service;

import org.androidpn.server.xmpp.XmppServer;

/**
 * This is a helper class to look up service objects.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ServiceLocator {

	public static String USER_SERVICE = "userService";
	public static String APN_USR_SERVICE = "apnUserService";
	public static String TOTAL_NOTIFICATION_SERVICE = "totalNotificationService";
	public static String USER_GROUP_SERVICE = "userGroupService";
	public static String GROUP_MEMBER_SERVICE = "groupMemberService";
	public static String GROUP_REQUEST_SERVICE = "groupRequestService";

	/**
	 * Generic method to obtain a service object for a given name.
	 * 
	 * @param name
	 *            the service bean name
	 * @return
	 */
	public static Object getService(String name) {
		return XmppServer.getInstance().getBean(name);
	}

	/**
	 * Obtains the user service.
	 * 
	 * @return the user service
	 */
	public static UserService getUserService() {
		return (UserService) XmppServer.getInstance().getBean(USER_SERVICE);
	}

	public static ApnUserService getApnUserService() {
		return (ApnUserService) XmppServer.getInstance().getBean(
				APN_USR_SERVICE);
	}

	public static TotalNotificationService getTotalNotificationService() {
		return (TotalNotificationService) XmppServer.getInstance().getBean(
				TOTAL_NOTIFICATION_SERVICE);
	}

	public static UserGroupService getUserGroupService() {
		return (UserGroupService) XmppServer.getInstance().getBean(
				USER_GROUP_SERVICE);
	}

	public static GroupMemberService getGroupMemberService() {
		return (GroupMemberService) XmppServer.getInstance().getBean(
				GROUP_MEMBER_SERVICE);
	}

	public static GroupRequestService getGroupRequestService() {
		return (GroupRequestService) XmppServer.getInstance().getBean(
				GROUP_REQUEST_SERVICE);
	}
}
