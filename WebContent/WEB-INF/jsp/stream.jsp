<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="org.springframework.security.ui.AbstractProcessingFilter"%>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter"%>
<%@ page import="org.springframework.security.AuthenticationException"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>Workast</title>
	    <link rel="stylesheet" type="text/css" href="js/extjs/resources/css/ext-all.css" />
	    <link rel="stylesheet" type="text/css" href="js/extjs/resources/css/xtheme-gray.css" />
	    <script type="text/javascript" src="js/extjs/adapter/ext/ext-base.js"></script>
	    <script type="text/javascript" src="js/extjs/ext-all.js"></script>
	    <script type="text/javascript" src="js/stream.js"></script>
	</head>
	<style>
		body {
			background: white url("img/loginBackground.jpg");
	        margin : 0px;
	        padding : 0px;
	        font-family : arial;
	
		}
	    h1 {
	        background : white;
	        border-bottom : 1px solid #ccc;
	        padding : 5px;
			color : #666;
	    }
	</style>
	<body>
		<h1><img src="img/loginTitle.png" />beta</h1>
		<div id="stream"></div>
		<a href='<c:url value="j_spring_security_logout"/>'>logout</a>
	</body>
</html>
