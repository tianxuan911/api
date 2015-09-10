<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
	<title>Home</title>
	<meta name="viewport" content="width=device-width,target-densitydpi=high-dpi,initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0, user-scalable=yes">
</head>
<body>
<h1>
	Hello world!  
</h1>
<P>  The time on the server is ${serverTime}. </P>
<script type="text/javascript"> 
var createMenu = function(){
	location.href='/api/createMenu';
}
</script>
<input type="submit" value="创建菜单"   onclick="createMenu();" />
<input type="submit" value="服务器时间" onclick="location.href='/api/servertime';" />
<input type="submit" value="发送消息"   onclick="location.href='/api/sendMsgToUser';" />
<input type="submit" value="绑定用户"   onclick="location.href='/api/bind';" />

</body>
</html>
