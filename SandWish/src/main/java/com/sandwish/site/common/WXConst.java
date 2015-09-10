package com.sandwish.site.common;

public class WXConst {
	public static final String APPID ="";
	public static final String APPSECRET = "";
	public static final String TOKEN = "";
	/**
	 * 获取微信服务器用户授权页面access_token URL
	 * @param appid
	 * @return
	 */
	public static String getAuth_UserInfoUrl(String appid){
		return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+
				"&redirect_uri=http%3a%2f%2fwww.aligoa.com%2fapi%2fbindUserInfo&response_type=code&scope=snsapi_userinfo&state=3434234234#wechat_redirect";
	}
	/**
	 * 获取刷新用户授权access_token URL	
	 * @param appid
	 * @param refresh_token
	 * @return
	 */
	public static String getAuth_RefreshUserInfoUrl(String appid,String refresh_token){
		return "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+appid+
				"&grant_type=refresh_token&refresh_token="+refresh_token;
	}
	/**
	 * 验证access_token是否有效url	
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static String isAuth_AccessTokenValidUrl(String access_token,String openid){
		return "https://api.weixin.qq.com/sns/auth?access_token="+access_token+"&openid="+openid;
	}
	/**
	 * 拉取用户信息url	
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static String getUserInfoUrl(String access_token,String openid){
		return "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
	}
	
	/**
	 * 获取创建菜单url
	 * @return
	 */
	public static String getCreateMenuUrl(){
		return "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+AccessTokenUtil.getAccessToken();
	}
	/**
	 * 获取发送消息至对应模板url
	 * @return
	 */
	public static String getSendMsgToUserUrl(){
		return "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+AccessTokenUtil.getAccessToken();
	}
	
	/**
	 * 获取微信菜单配置
	 * @return
	 */
	public static String getMenu(){
		return "{\"button\":"
					+ "[{\"name\":\"扫码\",\"sub_button\":["
						+ "{\"type\":\"scancode_waitmsg\",\"name\":\"扫码带提示\",\"key\":\"rselfmenu_0_0\",\"sub_button\":[]},"
						+ "{\"type\":\"scancode_push\",\"name\":\"扫码推事件\",\"key\":\"rselfmenu_0_1\",\"sub_button\":[]}"
					+ "]},"
					+ "{\"name\":\"发图\",\"sub_button\":["
						+ "{\"type\":\"pic_sysphoto\",\"name\":\"系统拍照发图\",\"key\":\"rselfmenu_1_0\",\"sub_button\":[]},"
						+ "{\"type\":\"pic_photo_or_album\",\"name\":\"拍照或者相册发图\",\"key\":\"rselfmenu_1_1\",\"sub_button\":[]},"
						+ "{\"type\":\"pic_weixin\",\"name\":\"微信相册发图\",\"key\":\"rselfmenu_1_2\",\"sub_button\":[]}"
						+ "]},"
					+ "{\"name\":\"其他\",\"sub_button\":["
						+ "{\"type\":\"click\",\"name\":\"点我有惊喜\",\"key\":\"rselfmenu_2_0\"},"
						+ "{\"type\":\"location_select\",\"name\":\"发送位置\",\"key\":\"rselfmenu_2_1\"},"
						+ "{\"type\":\"view\",\"name\":\"静默绑定\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WXConst.APPID+"&redirect_uri=http%3A%2F%2Fwww.aligoa.com%2Fapi%2Fbind&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"},"
						+ "{\"type\":\"view\",\"name\":\"授权绑定\",\"url\":\"http://www.aligoa.com/api/getUserInfo\"}"
//						+ "{\"type\":\"media_id\",\"name\":\"图片\",\"media_id\":\"MEDIA_ID1\"},"
//						+ "{\"type\":\"view_limited\",\"name\":\"图文消息\",\"media_id\":\"MEDIA_ID2\"}"
					+ "]}"		
				+ "]}";
	}
}
