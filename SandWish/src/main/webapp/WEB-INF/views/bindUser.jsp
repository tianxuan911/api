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
<script type="text/javascript"> 
</script>
<form action="/bindUser" method="post">
	<input type="text" value="用户名" name="userName" />
	<input type="password" value="密码" name="passWord"  />
	<input type="submit" value="绑定"  />
</form>
</body>
</html>
