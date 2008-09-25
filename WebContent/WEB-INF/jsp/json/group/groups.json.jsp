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
			<json:property name="created" value="${group.created}"/>
			<json:property name="priv" value="${group.private}"/>
			<json:property name="listed" value="${group.listed}"/>
			<json:property name="adminId" value="${group.adminId}"/>
			<json:property name="adminName" value="${group.adminName}"/>
			<json:property name="adminLastName1" value="${group.adminLastName1}"/>
			<json:property name="adminLastName2" value="${group.adminLastName2}"/>
			<json:property name="currentUserIsMember" value="${group.currentUserIsMember}"/>
			<json:property name="membersCount" value="${group.membersCount}"/>
			<json:property name="messagesCount" value="${group.messagesCount}"/>
		</json:object>
	</json:array>
</json:object>
