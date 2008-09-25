<%@ page contentType="text/html" %>
<%@ page pageEncoding="ISO-8859-1" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- --%>
<json:object escapeXml="false" prettyPrint="false">
	<json:object name="container">
		<json:object name="activities">
			<json:array name="activity" var="act" items="${stream.activities}">
				<json:object>
					<json:property name="id" value="${act.id}"/>
					<json:property name="postedTime" value="${act.postedTime}"/>
					<json:property name="type" value="${act.type}"/>
					<json:property name="renderedMessage" value="${act.renderedMessage}"/>
					<json:property name="parentId" value="${act.parentId}"/>
					<json:property name="replyToOwnerId" value="${act.replyToOwnerId}"/>
					<json:property name="replyToOwnerName" value="${act.replyToOwnerName}"/>
					<json:property name="replyToRenderedMessage" value="${act.replyToRenderedMessage}"/>
					<json:object name="group">
						<json:property name="id" value="${act.group.id}"/>
						<json:property name="name" value="${act.group.name}"/>
					</json:object>
					<json:object name="owner">
						<json:property name="id" value="${act.person.id}"/>
						<json:property name="name" value="${act.person.name}"/>
						<json:property name="lastName1" value="${act.person.lastName1}"/>
						<json:property name="lastName2" value="${act.person.lastName2}"/>
						<json:property name="hasPicture" value="${act.person.hasPicture}"/>
						<json:property name="currentActivityTitle" value="${act.person.currentActivityTitle}"/>
					</json:object>
					<json:object name="data">
						<c:choose>
							<c:when test="${act.type eq 'EVENT'}">
								<json:property name="startDate" value="${act.activityData.startDate}"/>
								<json:property name="endDate" value="${act.activityData.endDate}"/>
							</c:when>
							<c:when test="${act.type eq 'NEWGROUP'}">
								<json:property name="groupId" value="${act.activityData.groupId}"/>
								<json:property name="groupName" value="${act.activityData.groupName}"/>
							</c:when>
							<c:when test="${act.type eq 'STATUS'}">
								<json:array name="attachments" var="attach" items="${act.activityData.attachments}">
									<json:object>
										<json:property name="id" value="${attach.id}"/>
										<json:property name="name" value="${attach.name}"/>
										<json:property name="size" value="${attach.size}"/>
										<json:property name="contentType" value="${attach.contentType}"/>
									</json:object>
								</json:array>
							</c:when>
						</c:choose>
					</json:object>
					<json:object name="comments">
						<json:array name="activity" var="comment" items="${act.comments}">
	 						<json:object>
								<json:property name="id" value="${comment.id}"/>
								<json:property name="postedTime" value="${comment.postedTime}"/>
								<json:property name="type" value="${comment.type}"/>
								<json:property name="renderedMessage" value="${comment.renderedMessage}"/>
								<json:property name="parentId" value="${comment.parentId}"/>
								<json:property name="replyToOwnerId" value="${comment.replyToOwnerId}"/>
								<json:property name="replyToOwnerName" value="${comment.replyToOwnerName}"/>
								<json:property name="replyToRenderedMessage" value="${comment.replyToRenderedMessage}"/>
								<json:object name="group">
									<json:property name="id" value="${comment.group.id}"/>
									<json:property name="name" value="${comment.group.name}"/>
								</json:object>
								<json:object name="owner">
									<json:property name="id" value="${comment.person.id}"/>
									<json:property name="name" value="${comment.person.name}"/>
									<json:property name="lastName1" value="${comment.person.lastName1}"/>
									<json:property name="lastName2" value="${comment.person.lastName2}"/>
									<json:property name="hasPicture" value="${comment.person.hasPicture}"/>
									<json:property name="currentActivityTitle" value="${comment.person.currentActivityTitle}"/>
								</json:object>
								<json:object name="data">
									<c:choose >
										<c:when test="${comment.type eq 'EVENT'}">
											<json:property name="startDate" value="${comment.activityData.startDate}"/>
											<json:property name="endDate" value="${comment.activityData.endDate}"/>
										</c:when>
										<c:when test="${comment.type eq 'NEWGROUP'}">
											<json:property name="groupId" value="${comment.activityData.groupId}"/>
											<json:property name="groupName" value="${comment.activityData.groupName}"/>
										</c:when>
									</c:choose>
								</json:object>
							</json:object>
						</json:array>
					</json:object>
					<json:object name="activitytags">
						<json:array name="tag" var="acttag" items="${act.tags}">
	 						<json:object>
								<json:property name="id" value="${acttag.tag.id}"/>
								<json:property name="name" value="${acttag.tag.name}"/>
							</json:object>
						</json:array>
					</json:object>
				</json:object>
			</json:array>
		</json:object>
		<json:object name="person">
			<json:property name="id" value="${stream.person.id}"/>
			<json:property name="name" value="${stream.person.name}"/>
			<json:property name="lastName1" value="${stream.person.lastName1}"/>
			<json:property name="lastName2" value="${stream.person.lastName2}"/>
			<json:property name="title" value="${stream.person.title}"/>
			<json:property name="area" value="${stream.person.area}"/>
			<json:property name="city" value="${stream.person.city}"/>
			<json:property name="birthday" value="${stream.person.birthday}"/>
			<json:property name="email" value="${stream.person.email}"/>
			<json:property name="hasPicture" value="${stream.person.hasPicture}"/>
			<json:property name="currentActivityTitle" value="${stream.person.currentActivityTitle}"/>
		</json:object>
		<json:property name="pollInterval" value="${stream.pollInterval}"/>
	</json:object>
</json:object>
