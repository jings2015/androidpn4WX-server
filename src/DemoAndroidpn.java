import org.androidpn.server.xmpp.push.NotificationManager;

//
//  DemoAndroidpn.java
//  FeOA
//
//  Created by LuTH on 2012-3-26.
//  Copyright 2012 flyrise. All rights reserved.
//

public class DemoAndroidpn {

	public static void main(String[] args) {

		String apiKey = "1234567890";
		String title = "feoa";
		String message = "Hello World!";
		String uri = "http://www.baidu.com";
		String groupId = "0";
		String receiver = "AllUsers";
		String sender = "System";

		NotificationManager notificationManager = new NotificationManager();
		notificationManager.sendBroadcast(title, message, groupId, receiver, sender);
		// notificationManager.sendNotifcationToUser(apiKey, username, title,
		// message, uri);

	}
}
