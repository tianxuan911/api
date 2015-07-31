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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.sandwish.site.entity.User;
import com.sandwish.site.service.dao.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	public static final String APPID ="wx3d6354adb178ef15";
	public static final String APPSECRET = "592771a10582139181d758c6a827816e";
	public static  String ACCESS_TOKEN = null;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private UserService userService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		User u = userService.getUser("1");
		model.addAttribute("serverTime", u.getUser_login());
		//This is a dev-08!		
		return "home";
	}
	@RequestMapping(value = "/weixin", method = RequestMethod.GET)
	public ResponseEntity<String>  validWeixin(Locale locale,@RequestParam("echostr") String echostr, Model model) {
		logger.info("Welcome weixin! The client locale is {}.", locale);
		return getResponseEntity(echostr, HttpStatus.OK);
	}
	@RequestMapping(value = "/createMenu", method = RequestMethod.GET)
	public ResponseEntity<String>  validWeixin(Locale locale,Model model) {
		logger.info("Welcome weixin! The client locale is {}.", locale);
		URL url;
		ResponseEntity<String> re = null;
		String menu = "{\"button\":[{	\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"},{\"name\":\"菜单\",\"sub_button\":[{	\"type\":\"view\",\"name\":\"server\",\"url\":\"http://www.aligoa.com/servertime\"},{\"type\":\"view\",\"name\":\"视频\",\"url\":\"http://v.qq.com/\"},{\"type\":\"view\",\"name\":\"绑定\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3d6354adb178ef15&redirect_uri=http%3A%2F%2Fwww.aligoa.com%2Fbind&response_type=code&scope=snsapi_base&state=123#wechat_redirect\"}]}]}";
		try {
			getAccessToken();
			url = new URL("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+getAccessToken());
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
			re = getResponseEntity( "createMenu 错误"+e.getMessage(),HttpStatus.OK);
			e.printStackTrace();
		} catch (ProtocolException e) {
			re = getResponseEntity( "createMenu 错误"+e.getMessage(),HttpStatus.OK);
			e.printStackTrace();
		} catch (IOException e) {
			re = getResponseEntity( "createMenu 错误"+e.getMessage(),HttpStatus.OK);
			e.printStackTrace();
		}
		return re;
	}
	@RequestMapping(value = "/servertime", method = RequestMethod.GET)
	public String getServerTime(Locale locale,Model model) {
		logger.info(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Calendar.getInstance().getTime()));
		model.addAttribute("servertime", new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Calendar.getInstance().getTime()));
		return "servertime";
	}
	public String  getAccessToken() {
		logger.info("Welcome weixin! getAccessToken.");
		if(!StringUtils.isEmpty(ACCESS_TOKEN)){
        	return  ACCESS_TOKEN;
        }
		try {
			URL url = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET);
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
            ACCESS_TOKEN = json.getString("access_token");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ACCESS_TOKEN;
	}
	
	private <T> ResponseEntity<T> getResponseEntity(T body, HttpStatus statusCode){
		HttpHeaders headers = new HttpHeaders();   
        MediaType mediaType=new MediaType("text","html",Charset.forName("UTF-8"));   
        headers.setContentType(mediaType);   
		ResponseEntity<T> re = new ResponseEntity<T>(body,headers,statusCode);
		return re;
	}
	@RequestMapping(value = "/sendMsgToUser")
	public ResponseEntity<String>  sendMsgToUser(Locale locale,HttpServletRequest request) {
		logger.info("Welcome weixin! The client locale is {}.", locale);
		URL url;
		ResponseEntity<String> re = null;
		String msg = "{\"touser\":\""+request.getSession().getAttribute("openid")+"\",\"template_id\":\"wmWzkwTscAFI__1YLxE6uI8e27m_V0tVtCTw3R2lj3U\",\"url\":\"http://www.aligoa.com/\",\"topcolor\":\"#FF0000\",\"data\":{\"name\":{\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},\"msgtype\":{\"value\":\"宇宙快递业务\",\"color\":\"#173177\"}}}";
		try {
			getAccessToken();
			url = new URL("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+getAccessToken());
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");      
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
	@RequestMapping(value = "/bind")
	public String  bindUser(Locale locale,@RequestParam(value="code",required=false) String code,Model model,HttpServletRequest request) {
		logger.info("进入会掉方法",locale);
		String access_token = null;
		String openid = null;
		try {
			URL url = new URL("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APPID+"&secret="+APPSECRET+"&code="+code+"&grant_type=authorization_code");
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
            logger.info("进入会掉方法"+message,locale);
            JSONObject json = new JSONObject (message);
            access_token = json.getString("access_token");
            openid = json.getString("openid");
            model.addAttribute("openid", openid);
            model.addAttribute("access_token", access_token);
            request.getSession().setAttribute("openid", openid);
            request.getSession().setAttribute("access_token", access_token);
		} catch (IOException e) {
			logger.info("进入会掉方法"+e.getLocalizedMessage(),locale);
			e.printStackTrace();
		} catch (JSONException e) {
			logger.info("进入会掉方法"+e.getLocalizedMessage(),locale);
			e.printStackTrace();
		}
		logger.info("进入会掉方法返回bindUser",locale);
		return "bindUser";
	}
	@RequestMapping(value = "/bindUser", method = RequestMethod.POST)
	public String  bindUser(Locale locale,@RequestParam("userName") String userName,@RequestParam("passWord") String passWord, Model model,HttpServletRequest request, HttpServletResponse response) {
		logger.info("收到来自用户的绑定请求"+"用户名【"+userName+"】密码【"+passWord+"】"
	+"【OPENID】"+request.getSession().getAttribute("openid")+"【ACCESS_TOKEN】"+request.getSession().getAttribute("access_token"), locale);
		return "sendMsgPage";
	}
}
