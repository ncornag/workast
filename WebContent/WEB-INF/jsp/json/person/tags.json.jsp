<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- --%>
<json:object escapeXml="false" prettyPrint="false">
	<json:array name="tags" var="tag" items="${tags}">
		<json:object>
			<json:property name="id" value="${tag.id}"/>
			<json:property name="name" value="${tag.name}"/>
		</json:object>
	</json:array>
</json:object>
