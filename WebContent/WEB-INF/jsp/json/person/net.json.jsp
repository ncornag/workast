<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- --%>
<json:object escapeXml="false" prettyPrint="false">
	<json:object name="net">
		<json:array name="nodes" var="node" items="${net.nodes}">
			<json:object>
				<json:property name="id" value="${node.id}"/>
				<json:property name="type" value="${node.type}"/>
				<json:property name="fixed" value="${node.fixed}"/>
				<c:if test="${node.title != null}">
					<json:property name="title" value="${node.title}"/>
				</c:if>
				<c:if test="${node.data != null}">
					<json:property name="data" value="${node.data}"/>
				</c:if>
				<c:if test="${node.connections != null}">
					<json:array name="connections" var="conn" items="${node.connections}">
						<json:object>
							<json:property name="type" value="${conn.type}"/>
							<json:property name="id" value="${conn.id}"/>
							<json:property name="destinationId" value="${conn.destinationId}"/>
						</json:object>
					</json:array>
				</c:if>
			</json:object>
		</json:array>
	</json:object>
</json:object>
