package com.sandwish.site.common;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * 消息互动工具类
 * @author 旋
 *
 */
public class WXMsgUtil {
	private static Logger logger = LoggerFactory.getLogger(WXMsgUtil.class);
	/**
	 * TODO:根据微信服务器发来的消息请求，返回不同的响应字符串，字符串符合微信接口中规定的”回复用户消息“格式
	 * TODO:应该将不同事件的响应注册成Bean交由spring管理，各个时间动态调用bean完成响应
	 * @param request
	 * @return
	 */
	public static String getResponseMsgByRequest(HttpServletRequest request){
		Map<String,String> msg = WXMsgUtil.transXMLMsg(request);
		logger.debug("微信服务器推送消息："+msg.toString());
		String msgType = msg.get("MsgType");
		if(StringUtils.isEmpty(msgType)){
			return null;
		}
		//事件消息
		if("event".equals(msgType)){
			String eventType = msg.get("Event");
			//单击事件
			if("CLICK".equals(eventType)){
			//订阅
			}else if("subscribe".equals(eventType)){
				//扫描带参数二维码事件,如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
				if(msg.get("EventKey").startsWith("qrscene_")){
				//普通订阅	
				}else{
					
				}
			//取消订阅
			}else if("unsubscribe".equals(eventType)){
			//扫描带参数二维码事件
			}else if("SCAN".equals(eventType)){
			//上报地理位置事件
			}else if("LOCATION".equals(eventType)){
			//自定义菜单事件	
			}else if("CLICK".equals(eventType)){
				
			//点击菜单跳转链接时的事件推送
			}else if("VIEW".equals(eventType)){
				
			}
		//文本消息
		}else if("text".equals(msgType)){
			
		//图片消息
		}else if("image".equals(msgType)){
			
		//语音消息
		}else if("voice".equals(msgType)){
			
		//视频消息
		}else if("video".equals(msgType)){
			
		//小视频消息
		}else if("shortvideo".equals(msgType)){
			
		//地理位置消息
		}else if("location".equals(msgType)){
			
		//链接消息
		}else if("link".equals(msgType)){
			
		}
		return null;
	}
	
	/**
	 * 将微信服务器推送的XML消息转换成Map<二级标签名，对应值>
	 * @param request
	 * @return
	 */
	public static Map<String,String> transXMLMsg(HttpServletRequest request){
		Map<String,String> result = new HashMap<String,String>();
		try {
			Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(request.getInputStream());
			NodeList list = xml.getFirstChild().getChildNodes();
			Node node = null;
			for(int i = 0 ; i < list.getLength() ; i++){
				node = list.item(i);
				result.put(node.getNodeName(), node.getTextContent());
			}
			
		} catch (IOException e) {
			logger.debug("解析XMLerror",e);
		} catch (SAXException e) {
			logger.debug("解析XMLerror",e);
		} catch (ParserConfigurationException e){
			logger.debug("解析XMLerror",e);
		}
		return result;
	}
	
	/**
	 * SHA1签名，返回16进制字符串
	 * @param decript
	 * @return
	 */
	public static String SHA1(String decript) {
		MessageDigest digest;
		try {
			digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
