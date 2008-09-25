<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" >
	/* Global */
	var locale = '<%= LocaleContextHolder.getLocale().toString() %>';
	var context = '<c:url value="/"/>';
	var currentUser = null;
	var timeOffset = new Date().getTimezoneOffset();
	var youText = '<s:message code="stream.you"/>';
	
	/* Model 2 JS */
	var model = {};
	<c:if test="${model!=null}">
	    <c:forEach var="r" items="${model}">
	    model['<c:out value="${r.key}"/>'] = <c:out value="${fn:substring(r.value, fn:indexOf(r.value, ':')+1, fn:length(r.value)-1 )}" escapeXml="false"/>;
	    </c:forEach>
	</c:if>
</script>
<c:set var="locale" value="<%= LocaleContextHolder.getLocale().toString()%>"/>
<script type="text/javascript" src="<c:url value="/static/js/localization_${locale}.js"/>"></script>
