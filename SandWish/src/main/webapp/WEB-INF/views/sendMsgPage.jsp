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
<P>  openid is ${openid}. </P>
<P>  access_token is ${access_token}. </P>
<P>  绑定成功，点击下面的按钮，试试给自己发消息吧！ </P>
<script type="text/javascript"> 
</script>
<form action="/sendMsgToUser" method="post">
	<input type="submit" value="点击给自己发消息"  />
</form>
</body>
</html>
