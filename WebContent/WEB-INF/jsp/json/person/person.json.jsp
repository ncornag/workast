<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- --%>
<json:object escapeXml="false" prettyPrint="false">
	<json:object name="person">
		<json:property name="id" value="${person.id}"/>
		<json:property name="name" value="${person.name}"/>
		<json:property name="lastName1" value="${person.lastName1}"/>
		<json:property name="lastName2" value="${person.lastName2}"/>
		<json:property name="title" value="${person.title}"/>
		<json:property name="area" value="${person.area}"/>
		<json:property name="city" value="${person.city}"/>
		<json:property name="email" value="${person.email}"/>
		<json:property name="hasPicture" value="${person.hasPicture}"/>
		<json:property name="currentActivityTitle" value="${person.currentActivityTitle}"/>
	</json:object>
</json:object>


