<%@page import="edu.fudan.msg.constant.UserRole"%>
<%@ page import="edu.fudan.msg.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
User user = (User)session.getAttribute("user");
String title = "", username = "";
if(null == user){ response.sendRedirect("index.jsp"); }
else {
	username = user.getUsername();
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>MessagePlatform | Edit</title>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
</head>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

body {
	background: url('images/bg.png') repeat-x;
	background-size: 100% 100%;
	background-size: cover;
	font-family: Verdana, helvetica, arial, sans-serif;
	font-size: 16px; 
	font-size: 68.75%;
}

.main {
	width: 1000px;
	margin: 0 auto;
}
</style>
<body>
	<div class="main">
	</div>
</body>
</html>