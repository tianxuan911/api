package com.sandwish.site.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sandwish.site.common.NetWorkUtil;
import com.sandwish.site.common.WXConst;
import com.sandwish.site.common.WXMsgUtil;
import com.sandwish.site.common.WebAuthAccessTokenUtil;
import com.sandwish.site.entity.User;
import com.sandwish.site.service.dao.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/")
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private UserService userService;
	
	/**
	 * 主页
	 */
	@RequestMapping(value = "/home")
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		User u = userService.getUser("1");
		model.addAttribute("user_login", u.getUser_login());
		return "home";
	}
	
	/**
	 * 服务器验证及消息互动接口
	 * @param locale
	 * @param echostr
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "")
	public ResponseEntity<String>  entry(HttpServletRequest request,HttpServletResponse response,Locale locale
			,@RequestParam(value="signature",required=true) String signature
			,@RequestParam(value="timestamp",required=true) String timestamp
			,@RequestParam(value="nonce",required=true) String nonce
		,@RequestParam(value="echostr",required=false) String echostr) {
		if(RequestMethod.GET.toString().equals(request.getMethod())){
			logger.debug("验证服务器地址的有效性", locale);
			String[] tempStr = new String[]{WXConst.TOKEN,timestamp,nonce};
			Arrays.sort(tempStr);
			try {
				if(!WXMsgUtil.SHA1(tempStr[0]+tempStr[1]+tempStr[2]).equals(signature)){
					logger.debug("服务器地址无效["+NetWorkUtil.getIpAddress(request)+"]", locale);
				};
				return getResponseEntity(echostr, HttpStatus.OK);
			} catch (IOException e) {
				logger.debug("验证服务器地址的有效性出现错误",e);
			}
		}
		return getResponseEntity(WXMsgUtil.getResponseMsgByRequest(request), HttpStatus.OK);
	}
	
	/**
	 * 创建微信菜单
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/createMenu", method = RequestMethod.GET)
	public ResponseEntity<String>  createMenu(Locale locale,Model model) {
		logger.info("创建微信菜单", locale);
		URL url;
		ResponseEntity<String> re = null;
		String menu = WXConst.getMenu();
		
		try {
			url = new URL(WXConst.getCreateMenuUrl());
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");      
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");    
			http.setDoOutput(true);        
			http.setDoInput(true);
			http.connect();
			OutputStream os= http.getOutputStream();    
            os.write(menu.getBytes("UTF-8"));    
            os.flush();
            os.close();
            InputStream is =http.getInputStream();
            int size =is.available();
            byte[] jsonBytes =new byte[size];
            is.read(jsonBytes);
            String message=new String(jsonBytes,"UTF-8");
			return getResponseEntity("返回信息"+message,HttpStatus.OK);
		} catch (MalformedURLException e) {
			logger.info("createMenu 错误",e);
			re = getResponseEntity( "createMenu 错误"+e.getMessage(),HttpStatus.OK);
		} catch (ProtocolException e) {
			logger.info("createMenu 错误",e);
			re = getResponseEntity( "createMenu 错误"+e.getMessage(),HttpStatus.OK);
		} catch (IOException e) {
			logger.info("createMenu 错误",e);
			re = getResponseEntity( "createMenu 错误"+e.getMessage(),HttpStatus.OK);
		}
		return re;
	}
	
	/**
	 * 获取服务器时间
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/servertime", method = RequestMethod.GET)
	public String getServerTime(Locale locale,Model model) {
		logger.info("获取服务器时间"+new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Calendar.getInstance().getTime()));
		model.addAttribute("servertime", new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Calendar.getInstance().getTime()));
		return "servertime";
	}
	
	/**
	 * 模板消息发送接口
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sendMsgToUser")
	public ResponseEntity<String>  sendMsgToUser(Locale locale,HttpServletRequest request,@RequestParam(value = "name",required = false,defaultValue="太懒了，写点啥吧") String name,
			@RequestParam(value = "msgtype",required = false,defaultValue="吼吼哈嘿，开始用双截棍") String msgtype,@RequestParam(value = "openid",required = true) String openid) {
		logger.info("消息发送接口", locale);
		URL url;
		ResponseEntity<String> re = null;
		String msg = "{\"touser\":\""+openid+"\","
					+ "\"template_id\":\"OAUzIrj-KAV8A61KhCgLJKwUp8oz4qN6LXDyPdmbrfk\","
					+ "\"url\":\""+"http://www.aligoa.com"+"\","
					+ "\"topcolor\":\"#FF0000\","
					+ "\"data\":"
						+ "{"
							+ "\"first\":{\"value\":\""+"我们已收到您的货款，开始为您打包商品，请耐心等待: )"+"\",\"color\":\"#173177\"}"
							+ ",\"orderMoneySum\":{\"value\":\""+"30.00元"+"\",\"color\":\"#173177\"}"
							+ ",\"orderProductName\":{\"value\":\""+"我是商品名字"+"\",\"color\":\"#173177\"}"
							+ ",\"Remark\":{\"value\":\""+"如有问题请致电400-828-1878或直接在微信留言，小易将第一时间为您服务！"+"\",\"color\":\"#173177\"}"
						+ "}"
					+ "}";
		try {
			url = new URL(WXConst.getSendMsgToUserUrl());
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");      
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");    
			http.setDoOutput(true);        
			http.setDoInput(true);
			http.connect();
			OutputStream os= http.getOutputStream();    
            os.write(msg.getBytes("UTF-8"));    
            os.flush();
            os.close();
            InputStream is =http.getInputStream();
            int size =is.available();
            byte[] jsonBytes =new byte[size];
            is.read(jsonBytes);
            String message=new String(jsonBytes,"UTF-8");
            logger.info("消息体"+msg);
            logger.info("返回消息【"+message+"】");
			return getResponseEntity("返回信息"+message,HttpStatus.OK);
		} catch (MalformedURLException e) {
			re = getResponseEntity( "sendMsgToUser 错误"+e.getMessage(),HttpStatus.OK);
			e.printStackTrace();
		} catch (ProtocolException e) {
			re = getResponseEntity( "sendMsgToUser 错误"+e.getMessage(),HttpStatus.OK);
			e.printStackTrace();
		} catch (IOException e) {
			re = getResponseEntity( "sendMsgToUser 错误"+e.getMessage(),HttpStatus.OK);
			e.printStackTrace();
		}
		return re;
	}
	
	/**
	 * 静默绑定回调接口
	 * @param locale
	 * @param code
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bind")
	public String  bindUser(Locale locale,@RequestParam(value="code",required=false) String code,Model model,HttpServletRequest request) {
		logger.info("用户绑定",locale);
		String access_token = null;
		String openid = null;
		try {
			URL url = new URL("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WXConst.APPID+"&secret="+WXConst.APPSECRET+"&code="+code+"&grant_type=authorization_code");
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
            logger.info("用户绑定"+message);
            JSONObject json = new JSONObject (message);
            if(StringUtils.isEmpty(json.optString("errcode"))){
            	access_token = json.getString("access_token");
            	openid = json.getString("openid");
            	request.setAttribute("openid", openid);
            	request.setAttribute("access_token", access_token);
            }else{
            	model.addAttribute("errcode",json.optString("errcode"));
            	model.addAttribute("errmsg",json.optString("errmsg"));
            }
		} catch (IOException e) {
			logger.info("用户绑定出错",e);
		} catch (JSONException e) {
			logger.info("用户绑定出错",e);
		}
		return "bindUser";
	}
	/**
	 * 消息发送跳转接口
	 * @param locale
	 * @param userName
	 * @param passWord
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/bindUser", method = RequestMethod.POST)
	public String  bindUser(Locale locale,@RequestParam("userName") String userName,@RequestParam("passWord") String passWord,@RequestParam("openid") String openid, Model model,HttpServletRequest request, HttpServletResponse response) {
		logger.info("收到来自用户的绑定请求"
				+"用户名【"+userName+"】密码【"+passWord+"】"
				+"【OPENID】"+openid
				, locale);
		//TODO：这里验证用户名和密码的有效性，如果正确则做用户名和OPENID的绑定；否则继续验证用户名和密码		
		request.setAttribute("openid", openid);
		return "sendMsgPage";
	}
	/**
	 * 网页绑定回调接口
	 * @param locale
	 * @param code
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bindUserInfo")
	public String  bindUserInfo(Locale locale,@RequestParam(value="code",required=false) String code,Model model,HttpServletRequest request) {
		logger.info("用户绑定信息",locale);
		String access_token = null;
		String openid = null;
		try {
			URL url = new URL("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WXConst.APPID+"&secret="+WXConst.APPSECRET+"&code="+code+"&grant_type=authorization_code");
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
            logger.info("用户绑定信息"+message);
            JSONObject json = new JSONObject (message);
            if(StringUtils.isEmpty(json.optString("errcode"))){
            	access_token = json.getString("access_token");
            	openid = json.getString("openid");
            	request.getSession().setAttribute("openid", openid);
            	request.getSession().setAttribute("webauth_access_token", access_token);
            	logger.info("openid【"+openid+"】token【"+access_token+"】"+"用户sessionid【"+request.getSession().getId()+"】");
            	//TODO：将用户的网页授权access_token以及refresh_token按照openid存储，持久化到数据库            	
            	JSONObject user = WebAuthAccessTokenUtil.getUserInfo(access_token, openid);
    			
    			model.addAttribute("nickname", user.optString("nickname"));
    			model.addAttribute("webauth_access_token", access_token);
    			model.addAttribute("access_token", access_token);
    			model.addAttribute("openid", openid);
            }else{
            	model.addAttribute("errcode",json.optString("errcode"));
            	model.addAttribute("errmsg",json.optString("errmsg"));
            }
		} catch (IOException e) {
			logger.info("用户绑定信息出错",e);
		} catch (JSONException e) {
			logger.info("用户绑定信息出错",e);
		}
		return "bindUser";
	}
	
	/**
	 * 网页绑定接口
	 * @param locale
	 * @param code
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo")
	public void  getUserInfo(Locale locale,Model model,HttpServletRequest request,HttpServletResponse response) {
		logger.info("用户绑定信息",locale);
		String access_token = null;
		String openid = null;
		String refresh_token = null;
		try {
			//TODO:从数据库查询当前用户的openid,网页授权access_token,refresh_token。如果没有这些信息，
			//直接跳转到腾讯授权页面，执行授权	
			response.sendRedirect(WXConst.getAuth_UserInfoUrl(WXConst.APPID));
			return;
		}catch (IOException e) {
			logger.debug("跳转微信授权页面error", e);
		}
		//TODO:查询腾讯服务器，当前access_token是否有效，如果无效调用refresh_token刷新授权
		if(WebAuthAccessTokenUtil.isAuth_AccessTokenValid(access_token, openid)){
			//获取用户信息，放到request中，传递给下个页面
			JSONObject user = WebAuthAccessTokenUtil.getUserInfo(access_token, openid);
			model.addAttribute("nickname", user.optString("nickname"));
			try {
				request.getRequestDispatcher("/userInfo").forward(request, response);
				return;
			} catch (ServletException e) {
				logger.debug("转发至/userInfo发生error", e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.debug("转发至/userInfo发生error", e);
				e.printStackTrace();
			}
			return;
		}else{
			String result = WebAuthAccessTokenUtil.RefreshAuth_Token(refresh_token,WXConst.APPID);
			//如果调用refresh_token刷新授权，返回结果提示refresh_token过期，跳转到微信授权页面，执行授权	
			if("42002".equalsIgnoreCase(result)){
				try {
					response.sendRedirect(WXConst.getAuth_UserInfoUrl(WXConst.APPID));
				} catch (IOException e) {
					logger.debug("重定向至微信授权页面error", e);
				}
				return;
			}else{
				try {
					request.getRequestDispatcher("/getUserInfo").forward(request, response);
				} catch (IOException e) {
					logger.debug("转发至/getUserInfo发生error", e);
				} catch (ServletException e) {
					logger.debug("转发至/getUserInfo发生error", e);
				}
			}
		}
	}
	
	private static <T> ResponseEntity<T> getResponseEntity(T body, HttpStatus statusCode){
		HttpHeaders headers = new HttpHeaders();
        MediaType mediaType=new MediaType("text","html",Charset.forName("UTF-8"));   
        headers.setContentType(mediaType);   
		ResponseEntity<T> re = new ResponseEntity<T>(body,headers,statusCode);
		return re;
	}
	
	private static <T> ResponseEntity<T> getResponseEntity(MediaType mediaType,T body, HttpStatus statusCode){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType(mediaType.getType(), mediaType.getSubtype(), Charset.forName("UTF-8")));   
		ResponseEntity<T> re = new ResponseEntity<T>(body,headers,statusCode);
		return re;
	}
}
