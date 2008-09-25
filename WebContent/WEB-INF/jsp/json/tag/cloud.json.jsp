<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- --%>
<json:object escapeXml="false" prettyPrint="false">
	<json:array name="cluod" var="tag" items="${tags}">
		<json:object>
			<json:property name="tag" value="${tag.name}"/>
			<json:property name="count" value="${tag.count}"/>
		</json:object>
	</json:array>
</json:object>
