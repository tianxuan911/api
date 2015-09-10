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
<P>  openid is ${openid}. </P>
<P>  绑定成功，点击下面的按钮，试试给自己发消息吧！ </P>
<script type="text/javascript"> 
</script>
<form action="/api/sendMsgToUser" method="post">
	<input type="text" value="懒人专用，啥也别写"  name="name"/>
	<input type="text" value="懒人专用，啥也别写哈"  name="msgtype" />
	<input type="hidden" value="${openid}" name="openid"  /> 
	<input type="submit" value="点击给自己发消息"  />
</form>
</body>
</html>
