<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>

<div id="footer">
	<!-- 
	<div id="logo"></div>
	 -->
	<div id="changer">
		<a class="styleswitch" rel="pink" href="#"><img height="24" width="27" alt="pink" src="<c:url value="/static/img/themes/style_pink.gif"/>"/></a>
		<a class="styleswitch" rel="purple" href="#"><img height="24" width="27" alt="purple" src="<c:url value="/static/img/themes/style_purple.gif"/>"/></a>
		<a class="styleswitch" rel="blue" href="#"><img height="24" width="27" alt="blue" src="<c:url value="/static/img/themes/style_blue.gif"/>"/></a>
		<a class="styleswitch" rel="green" href="#"><img height="24" width="27" alt="green" src="<c:url value="/static/img/themes/style_green.gif"/>"/></a>
	</div>
	<div id="copyright">
		<a href="http://code.google.com/p/workast/">Powered by Workast</a>
		| 
		<c:set var="locale" value="<%= LocaleContextHolder.getLocale().toString()%>"/>
		<c:if test="${'en'==locale}">
			<a class="language" href="?locale=es">Español</a>
		</c:if>
		<c:if test="${'es'==locale}">
			<a class="language" href="?locale=en">English</a>
		</c:if>
		|
	</div>
</div>