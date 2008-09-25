<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- --%>
<json:object escapeXml="false" prettyPrint="false">
	<json:array name="groups" var="group" items="${groups}">
		<json:object>
			<json:property name="id" value="${group.id}"/>
			<json:property name="name" value="${group.name}"/>
		</json:object>
	</json:array>
</json:object>
