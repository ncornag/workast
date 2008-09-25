<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- --%>
<json:object escapeXml="false" prettyPrint="false">
	<json:array name="persons" var="person" items="${persons}">
		<json:object>
			<json:property name="id" value="${person.id}"/>
			<json:property name="name" value="${person.name}"/>
			<json:property name="lastName1" value="${person.lastName1}"/>
			<json:property name="lastName2" value="${person.lastName2}"/>
		</json:object>
	</json:array>
</json:object>
