<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter"%>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter"%>
<%@ page import="org.springframework.security.AuthenticationException"%>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<html>
	<head>
		<title><s:message code="corp.name"/></title>

		<link rel="icon" type="image/png" href="<c:url value="/favicon.ico"/>" />

		<link href="<c:url value="/static/css/grid.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/jquery/ui/default/ui.core.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/jquery/ui/default/ui.datepicker.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/jquery/ui/default/ui.dialog.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/jquery/ui/default/ui.theme.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/jquery/jquery.autocomplete.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/markitup/skins/simple/style.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/markitup/sets/wiki/style.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/application.css"/>" media="screen" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/css/buttons.css"/>" media="screen" rel="stylesheet" type="text/css" />

		<script type="text/javascript" src="<c:url value="/static/js/css_browser_selector.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery-1.3.1.min.js"/>"></script>

		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.form.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.template.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.autocomplete.pack.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.validate.pack.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.sparkline.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.ui.core.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.ui.datepicker.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.ui.dialog.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.ui.effects.core.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.binder-0.3.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.ocupload-1.1.2.packed.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/markitup/jquery.markitup.pack.js"/>"></script>
	    <script type="text/javascript" src="<c:url value="/static/js/utilities.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/styleswitch.js"/>"></script>

		<tiles:insertAttribute name="main.js"/>
		<script type="text/javascript" src="<c:url value="/static/js/markitup/sets/wiki/set.js"/>"></script>

		<link href="<c:url value="/static/css/blue.css"/>"     media="screen" rel="stylesheet" type="text/css" title="blue" />
		<link href="<c:url value="/static/css/purple.css"/>"   media="screen" rel="alternate stylesheet" type="text/css" title="purple" />
		<link href="<c:url value="/static/css/pink.css"/>"     media="screen" rel="alternate stylesheet" type="text/css" title="pink" />
		<link href="<c:url value="/static/css/green.css"/>"    media="screen" rel="alternate stylesheet" type="text/css" title="green" />
		<link href="<c:url value="/static/css/defaults.css"/>" media="screen" rel="stylesheet" type="text/css" />	

	</head>
	<body>
		<script type="text/javascript" src="<c:url value="/static/js/wz/wz_tooltip.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/wz/wz_tip_balloon.js"/>"></script>
		<div id="wrapper">
			<tiles:insertAttribute name="nav" ignore="true"/>
			<tiles:insertAttribute name="top"/>
			<div id="main_line" class="upper_line">
				<div id="two_left">
					<tiles:insertAttribute name="topLeft" ignore="true"/>
				</div>		
				<div id="two_right">
					<tiles:insertAttribute name="topRight" ignore="true"/>
				</div>		
			</div>
			<div id="main_line" class="bottom_line">
				<div id="two_left">
					<tiles:insertAttribute name="mainLeft" ignore="true"/>
				</div>		
				<div id="two_right">
					<tiles:insertAttribute name="mainRight" ignore="true"/>
					<tiles:insertAttribute name="mainRight2" ignore="true"/>
				</div>		
			</div>
			<tiles:insertAttribute name="footer"/>
		</div>
	</body>
</html>

