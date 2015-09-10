<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
	<title>Home</title>
	<meta name="viewport" content="width=device-width,target-densitydpi=high-dpi,initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0, user-scalable=yes">
</head>
<body>
<P>  nickname is ${nickname}. </P>
<P>  openid is ${openid}. </P>
<P>  access_token is ${webauth_access_token}. </P>
</body>
</html>
