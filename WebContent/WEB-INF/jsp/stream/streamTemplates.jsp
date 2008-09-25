<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<textarea id='streamTemplate' style='display:none'>
	<li class='conversation_piece' id='conversation_piece_{id}'>
		<div class='col_1'>
			<a class="TypeLinkProfile" id="activityProfilePicture_{ownerId}" href="#" onMouseOver="Tip('{iconTitle}', BALLOON, true, ABOVE, true, OFFSETX, -30)" onmouseout="UnTip()"><span></span><img class="col_2 mugshot" src="<c:url value="/static/img/profiles/profile_{ownerPicture}.jpg"/>"/></a>
		</div>
		
		<div class='col_0 {activityIconClass} ml15'/>

		<div class='col_14 ml5'>
			<div class='title'>
				<a class="TypeLinkProfile" id="activityProfile_{ownerId}" href='#' >{ownerName}</a> {message}
			</div>
			<div class="col_14" id="actAttachmentListDiv_{id}" style="display:none"><ul id="actAttachmentList_{id}"></ul></div>
			<div class="actions">
				<span class="prettyTime" id="conversation_pice_time_{id}" title="{fullPostedTime}" timeData="{timeData}">  {prettyPostedTime}</span>
				<span class="separator" style="display:{groupInShow}">&#8226; <s:message code="stream.in" /> <a class="comment-from TypeLinkGroup" id="activityGroup_{groupId}" href="#" style="display:{groupInShow}"> {groupName}</a></span>
				<span class="separator">&#8226; <a class="comment-reply" id="commentReplyAction_{id}" href="#"><s:message code="stream.comment"/></a></span>
				<span class="separator">&#8226; <a class="addTag" id="addTag_{id}" href="#" onclick=""><s:message code="stream.tag"/></a></span>
				<div id='showTagsDivPlaceholder_{id}'></div>
				<div id="editTagsDiv_{id}" style='display:none' class='editTagsDiv'>
					<ul class="filterTagList">
						<li class="scope">
							<a id="currscope" class="currscope" href="#">tags</a>
						</li>
						<li class="box">
							<form action="<c:url value="/data/activity/{id}/tag"/>" name="magicboxform_{id}" id="magicboxform_{id}" class="editTagForm on" method="post">
								<input type="text" size="25" name="name" id="name" class="addtagEdit">
								<input type="submit" class="addtagEditSubmit"/>
							</form>
						</li>
						<li class="tags">
							<ul id="editTagsList_{id}" />
						</li>
					</ul>
				</div>
			</div>
			<div class="details" id="action_container_{id}" style="display: none;">
				<div class="comment_form" id="comment_form_{id}" style="display: none;">
					<h4>
						<s:message code="form.comment.title"/>
					</h4>
					<form action="<c:url value="/data/activity/{id}/comment"/>" class="new_comment" id="commentForm_{id}" name="commentForm_{id}" method="post">
						<div class="eminem">
							&lt;textarea cols="60" id="comment" name="comment" rows="4"&gt;&lt;/textarea&gt;
						</div>
						<input type="submit" value="<s:message code="stream.comment.button" />" id="submit" name="submit" onclick="$('#commentForm_{id}').submit();return false;"/>
					</form> 
				</div>
			</div>
			<div id="commentsDisplayDiv_{id}" class="col_14" style="display:none"><a id="commentsDisplayLink_{id}" class="collapsedThread" href=""></a></div>
			<div id="commentsDiv_{id}" class="col_14" style="display:none"><ul id='commentsList_{id}' class='discuss'></ul></div>
		</div>
	</li>
</textarea>

<textarea id='commentTemplate' style='display:none'>
	<li class='response '>
		<div class='col_14 response'>
			<div class='col_1'>
				<a class="TypeLinkProfile" id="activityProfilePicture_{commenterId}" href="#" onMouseOver="Tip('{iconTitle}', BALLOON, true, ABOVE, true, OFFSETX, -30)" onmouseout="UnTip()"><img class="col_2_int mugshot" src="<c:url value="/static/img/profiles/profile_{commenterPicture}.jpg"/>"/></a>
			</div>
			<div class='col_12'>
				<div id='comment_{id}'>
					<div class='title'>
						<a class="profile TypeLinkProfile" id="activityProfile_{commenterId}" href="#">{commenter}</a> {message}
					</div>
					<div class='actions'>
						<span class="prettyTime" id="comment_time_{id}" title="{fullPostedTime}" timeData="{timeData}">{prettyPostedTime}</span>
					</div>
				</div>
			</div>
		</div>
	</li>
</textarea>

<textarea id='tagsTemplate' style='display:none'>
	<li class="activityTagItem off" id="tag_{id}_{name}">
		<a href="#" class="activityTagLink"><span class="activityTagName">{name}</span></a>
	</li>
</textarea>

<textarea id='tagsFilterTemplate' style='display:none'>
	<li class="tag" id="filterTag_{name}">
		<a href="#" class="onlytag">{name}</a>
	</li>
</textarea>

<textarea id='tagsEditTemplate' style='display:none'>
	<li class="tag removetag" id="editTag_{id}_{name}">
		<a href="#" class="onlytag">{name}</a>
	</li>
</textarea>

<textarea id="activityAttachmentDisplay" style="display:none">
	<li><a class="attachIcon" href="<c:url value="/data/download/attachment/{actId}/{id}"/>">{name}</a> ({size})</li>
</textarea>

<textarea id="activityAttachmentDisplayAsImage" style="display:none">
	<li><a href="<c:url value="/data/download/attachment/{actId}/{id}"/>"><img class="attachImage" title="{name} ({size})" src="<c:url value="/data/download/attachment/{actId}/{id}"/>"/></a></li>
</textarea>

