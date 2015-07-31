<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>
<P>  The time on the server is ${serverTime}. </P>
<script type="text/javascript"> 
var createMenu = function(){
	location.href='/createMenu';
}
</script>
<input type="submit" value="创建菜单"   onclick="createMenu();" />
<input type="submit" value="服务器时间" onclick="location.href='/servertime';" />
<input type="submit" value="发送消息"   onclick="location.href='/sendMsgToUser';" />
<input type="submit" value="绑定用户"   onclick="location.href='/bind';" />

</body>
</html>
