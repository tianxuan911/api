package com.sandwish.site.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 普通access_token
 * 与网页授权access_token不同
 * 详情参见http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html
 * @author 旋
 *
 */
public class AccessTokenUtil {
	private static final Logger logger = LoggerFactory.getLogger(AccessTokenUtil.class);
	private static String ACCESS_TOKEN = null;
	private static long EXPIRES_IN = Long.MAX_VALUE; 
	/**
	 * 获取有效的AccessToken
	 * @return 
	 */
	public static String getAccessToken(){
		if(StringUtils.isEmpty(ACCESS_TOKEN) || isExpired()){
			return getAccessTokenFromWeixinServer();
		}
		return ACCESS_TOKEN;
	}
	/**
	 * AccessToken是否过期，过期返回true，否则返回false
	 * @return
	 */
	private static boolean isExpired(){
		if(System.currentTimeMillis()<EXPIRES_IN){
			return false;
		}
		return true;
	}
	/**
	 * 初始化access缓存
	 * @param access_token
	 * @param expires_in
	 */
	private static void iniTokenCache(String access_token,long expires_in){
		 ACCESS_TOKEN = access_token;
         EXPIRES_IN = System.currentTimeMillis()+expires_in*1000;
	}
	/**
	 * 从服务器获取有效的AccessToken
	 * @return
	 */
	private static String  getAccessTokenFromWeixinServer() {
		logger.info("从服务器获取有效的AccessToken");
		try {
			URL url = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WXConst.APPID+"&secret="+WXConst.APPSECRET);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");          
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");    
            http.setDoOutput(true);        
            http.setDoInput(true);
            http.connect();
            InputStream input = http.getInputStream();
            byte[] inputbs = new byte[input.available()];
            input.read(inputbs);
            String message=new String(inputbs,"UTF-8");
            JSONObject json = new JSONObject (message);
            iniTokenCache(json.getString("access_token"),json.getLong("expires_in"));
		} catch (IOException e) {
			logger.info("从服务器获取有效的AccessToken出现错误",e);
		} catch (JSONException e) {
			logger.info("从服务器获取有效的AccessToken出现错误",e);
		}
		return ACCESS_TOKEN;
	}
}
