package com.sandwish.site.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
/**
 * 网页授权access_token工具类
 * 微信网页授权是通过OAuth2.0机制实现的，在用户授权给公众号后，公众号可以获取到一个网页授权特有的接口调用凭证
 * （网页授权access_token），通过网页授权access_token可以进行授权后接口调用，如获取用户基本信息；
 * 每个openid对应一个网页授权access_token
 * @author 旋
 *
 */
public class WebAuthAccessTokenUtil {
	private static final Logger logger = LoggerFactory.getLogger(WebAuthAccessTokenUtil.class);
	
	/**
	 * 验证access_token是否有效
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static boolean isAuth_AccessTokenValid(String access_token,String openid){
		URL url;
		try {
			url = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WXConst.APPID+"&secret="+WXConst.APPSECRET);
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
	        String errcode = json.optString("errcode");
	        if("40003".equalsIgnoreCase(errcode)){
	        	return false;
	        }
	        else{
	        	return true;
	        }
		} catch (MalformedURLException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (ProtocolException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (IOException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (JSONException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		}
		
		return false;
	}
	
	/**
	 * 刷新网页授权accessToken，如果刷新成功返回accessToken;如果服务器返回错误信息，返回errcode,详见微信官方api http://mp.weixin.qq.com/wiki/17/fa4e1434e57290788bde25603fa2fcbd.html;否则返回null;
	 * @param refresh_token
	 * @param appid
	 * @return
	 */
	public static String RefreshAuth_Token(String refresh_token,String appid){
		try {
			URL url = new URL(WXConst.getAuth_RefreshUserInfoUrl(appid,refresh_token));
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
			String errcode = json.optString("errcode");
			if(!StringUtils.isEmpty(errcode)){
				logger.debug("刷新网页授权"+errcode);
				return errcode;
			}
			//TODO：持久化到数据库
			return json.optString("access_token");
		} catch (MalformedURLException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (ProtocolException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (IOException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (JSONException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		}
		return null;
	}
	/**
	 * 拉取用户信息,获取成功，返回用户信息json对象，否则返回null
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static JSONObject getUserInfo(String access_token,String openid){
		try {
			URL url = new URL(WXConst.getUserInfoUrl(access_token, openid));
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
			String errcode = json.optString("errcode");
			if(!StringUtils.isEmpty(errcode)){
				logger.debug("获取用户信息出错"+errcode);
				return null;
			}
			return json;
		} catch (MalformedURLException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (ProtocolException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (IOException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		} catch (JSONException e) {
			logger.debug("WebAuthAccessTokenUtil",e);
		}
		return null;
	}
	
	
}
