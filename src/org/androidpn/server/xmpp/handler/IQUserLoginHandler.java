package org.androidpn.server.xmpp.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidpn.server.model.ApnUser;
import org.androidpn.server.model.User;
import org.androidpn.server.service.ApnUserService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.ErrorCode;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

import com.wyx.utils.MD5;

public class IQUserLoginHandler extends IQHandler {

	private static final String ELEMENT_NAME = "userlogin";
	private static final String NAMESPACE = "androidpn:iq:userlogin";
	private UserService userService;
	private ApnUserService apnUserService;

	public IQUserLoginHandler() {
		userService = ServiceLocator.getUserService();
		apnUserService = ServiceLocator.getApnUserService();
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		System.out.println("in IQUserLoginHandler ");
		boolean isTimeout = false;
		IQ reply = null;
		Element loginResult = DocumentHelper.createElement(QName.get(
				ELEMENT_NAME, NAMESPACE));
		loginResult.addElement("method").setText(ELEMENT_NAME);

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			return null;
		}
		if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
			if (IQ.Type.set.equals(packet.getType())) {
				Element element = packet.getChildElement();
				String account = element.elementText("account");
				String password = element.elementText("password");
				String timestamp = element.elementText("timestamp");
				String clientId = element.elementText("clientId");

				try {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					Date clientDate;
					clientDate = sdf.parse(timestamp);
					Date serverDate = new Date();
					long between = serverDate.getTime() - clientDate.getTime();
					between = between / (60 * 1000);

					System.out.println("clientDate " + clientDate);
					System.out.println("serverDate" + serverDate);
					if (between > 5) {
						// 登录异常（与服务器时间相差5分钟）
						loginResult.addElement("ecode").setText(
								ErrorCode.LOGIN_ERROR_303);
						loginResult.addElement("emsg").setText(
								ErrorCode.getEmsg(ErrorCode.LOGIN_ERROR_303));
						isTimeout = true;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (!isTimeout) {
					if (!userService.exists(account)) {
						// 账号不存在
						loginResult.addElement("ecode").setText(
								ErrorCode.LOGIN_ERROR_302);
						loginResult.addElement("emsg").setText(
								ErrorCode.getEmsg(ErrorCode.LOGIN_ERROR_302));

					} else {

						User user = userService.getUserByAccount(account);
						MD5 md5 = new MD5();
						String psdInServ = md5.getMD5(user.getPassword()
								+ timestamp);
						if (password.equals(psdInServ)) {
							// 登录成功
							// 解除clientId原本绑定的用户
							User oldUser = userService
									.getUserByClientId(clientId);
							if (oldUser != null) {
								oldUser.setClientId(null);
								userService.updateUser(oldUser);
							}
							// 记录新的clientId关系
							user.setClientId(clientId);
							userService.updateUser(user);

							ApnUser apnUser = apnUserService
									.getApnUserByClientId(clientId);
							if (apnUser == null) {
								System.out.println("apnuser is null");
								System.out.println(clientId);
								return null;
							} else {
								apnUser.setAccount(account);
								apnUserService.updateApnUser(apnUser);
							}

							loginResult.addElement("ecode").setText(
									ErrorCode.LOGIN_SUCCEED);
							loginResult.addElement("emsg").setText(
									ErrorCode.getEmsg(ErrorCode.LOGIN_SUCCEED));
						} else {
							// 密码错误
							loginResult.addElement("ecode").setText(
									ErrorCode.LOGIN_ERROR_301);
							loginResult
									.addElement("emsg")
									.setText(
											ErrorCode
													.getEmsg(ErrorCode.LOGIN_ERROR_301));
						}
					}
				}

				reply = new IQ();
				reply.setType(IQ.Type.set);
				reply.setChildElement(loginResult);
				System.out.println("reply userlogin ");
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
