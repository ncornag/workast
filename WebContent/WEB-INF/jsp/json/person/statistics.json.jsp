<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- --%>
<json:object escapeXml="false" prettyPrint="false">
	<json:object name="personStatistic">
		<json:property name="dateFrom" value="${personStatistic.dateFrom}"/>
		<json:array name="activityCount" var="count" items="${personStatistic.activityCount}">
			<json:property value="${count}"/>
		</json:array>
	</json:object>
</json:object>


