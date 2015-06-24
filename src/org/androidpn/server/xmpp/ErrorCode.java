package org.androidpn.server.xmpp;

public class ErrorCode {

	public static final String REGISTER_SUCCEED = "200";
	public static final String REGISTER_ERROR_201 = "201";
	public static final String LOGIN_SUCCEED = "300";
	public static final String LOGIN_ERROR_301 = "301";
	public static final String LOGIN_ERROR_302 = "302";
	public static final String LOGIN_ERROR_303 = "303";
	public static final String CREATE_GROUP_SUCCEED = "400";
	public static final String CREATE_GROUP_ERROR_401 = "401";
	public static final String SEND_MESSAGE_4_GROUP_SUCCEED = "500";
	public static final String SEND_MESSAGE_4_GROUP_501 = "501";
	public static final String JOIN_GROUP_SUCCEED = "600";
	public static final String JOIN_GROUP_601 = "601";
	public static final String JOIN_GROUP_602 = "602";
	public static final String JOIN_GROUP_603 = "603";
	public static final String HANDLE_GROUP_REQUEST_AGREE = "700";
	public static final String HANDLE_GROUP_REQUEST_DISAGREE = "701";
	public static final String HANDLE_GROUP_REQUEST_702 = "702";
	public static final String CHANGE_PUSHABLE_4_GROUP_800 = "800";
	public static final String CHANGE_PUSHABLE_4_GROUP_801 = "801";
	public static final String CHANGE_PUSHABLE_4_GROUP_802 = "802";
	public static final String CHANGE_PUSHABLE_4_GROUP_803 = "803";
	public static final String CHANGE_PUSHABLE_4_GROUP_804 = "804";
	public static final String CHANGE_PUSHABLE_4_GROUP_805 = "805";
	public static final String OUT_MEMBER_4_GROUP_SUCCEED = "900";
	public static final String OUT_MEMBER_4_GROUP_901= "901";
	public static final String OUT_MEMBER_4_GROUP_902 = "902";
	
	
	
	

	public static String getEmsg(String ecode) {
		int ecodeInt = Integer.parseInt(ecode);
		switch (ecodeInt) {
		case 200:
			return "注册成功，请登录";
		case 201:
			return "注册账户已存在";
		case 300:
			return "登录成功";
		case 301:
			return "密码错误";
		case 302:
			return "账号不存在";
		case 303:
			return "登录异常（与服务器时间相差5分钟）";
		case 400:
			return "创建成功";
		case 401:
			return "创建失败";
		case 500:
			return "信息发送成功";
		case 501:
			return "未知错误，信息发送失败";
		case 600:
			return "请求已发送，等待群主确认";
		case 601:
			return "您已加入该群，无需重复申请";
		case 602:
			return "请求已存在，无需重复请求";
		case 603:
			return "未知错误，请求失败";
		case 700:
			return "通过成功";
		case 701:
			return "拒绝成功";
		case 702:
			return "操作失败";
		case 800:
			return "开启发送权限成功";
		case 801:
			return "取消发送权限成功";
		case 802:
			return "用户已有发送权限，无需重复开启";
		case 803:
			return "用户没有发送权限，无需重复关闭";
		case 804:
			return "未知错误，操作失败";
		case 805:
			return "您没有权限操作";
		case 900:
			return "踢出成功";
		case 901:
			return "您没有权限操作";
		case 902:
			return "用户不存在";
			
		default:
			return "";

		}
	}

}
