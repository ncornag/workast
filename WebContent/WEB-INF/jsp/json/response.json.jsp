<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    response.setContentType(((JsonResponse)request.getAttribute("response")).getContentType());
%>
<%-- --%>

<%@page import="es.workast.core.json.JsonResponse"%><json:object escapeXml="false" prettyPrint="false">
	<json:object name="response">
		<json:property name="status" value="${response.status}"/>
		<json:array name="errors" var="error" items="${response.errors}">
			<json:property value="${error}"/>
		</json:array>
		<json:array name="fieldErrors" var="data" items="${response.fieldErrors}">
			<json:object>
				<json:property name="key" value="${data.key}"/>
				<json:property name="value" value="${data.value}"/>
			</json:object>
		</json:array>
		<json:array name="data" var="data" items="${response.data}">
			<json:object>
				<json:property name="key" value="${data.key}"/>
				<json:property name="value" value="${data.value}"/>
			</json:object>
		</json:array>
	</json:object>
</json:object>
