<%@page import="edu.fudan.msg.constant.UserRole"%>
<%@ page import="edu.fudan.msg.pojo.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	User user = (User) session.getAttribute("user");
	if (null != user) {
		response.sendRedirect("admin.jsp");
	}
%>
<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>学生综合管理平台</title>
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="js/index.js"></script>
    <link href="css/index.css" rel="stylesheet">
  </head>
  <body onkeydown="if (event.keyCode==13) {document.getElementById('enter').click();}">
    <div class="container">
      <form class="form-signin" role="form">
        <h2 class="form-signin-heading">学生综合管理平台</h2>
        <input type="text" class="form-control user" id="1" placeholder="用户名" autofocus="">
        <input type="password" class="form-control pwd" id="2" placeholder="密码">
        <button class="btn btn-lg btn-primary btn-block" type="button" id="enter" onClick="login();">登陆</button>
        <a href="register.html">去注册</a>
      </form>


    </div>
  </body>
</html>