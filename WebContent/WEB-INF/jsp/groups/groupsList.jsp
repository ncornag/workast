<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<div class="col_4">
	<h2><span><s:message code="group.title" /></span></h2>
</div>
<div class="col_4">
	<a class='btn' href="<c:url value="/group/new"/>" id="createGroup"><span><span><s:message code="group.create" /></span></span></a>
</div>

<div class="col_16">
	<div id="groupsNav">
		<ul id="groupsNavList"></ul>
	</div>
	<div id="groupsHeader">
		<ul id="groupsHeaderList"></ul>
	</div>
	<div id="groups">
		<ul id="groupsList"></ul>
	</div>
</div>

<textarea id='groupNavTemplate' style='display:none'>
	<li id="groupNav_{nav}">
		<div class="groupNavLetter">
			<a href="#">{nav}</a>
		</div>
	</li>
</textarea>

<textarea id='groupHeaderTemplate' style='display:none'>
	<li id="groupHeader">
		<div class="col_16 groupHeader">
			<div class="col_4 header">{groupNameTitle}</div>
			<div class="col_3 header">{adminNameTitle}</div>
			<div class="col_2 header right">{createdTitle}</div>
			<div class="col_2 header right">{messagesTitle}</div>
			<div class="col_2 header right">{membersTitle}</div>
			<div class="col_2 header"></div>
			<div class="col_0 header"></div>
		</div>
	</li>
</textarea>

<textarea id='groupRowTemplate' style='display:none'>
	<li id="groupRow_{groupId}">
		<div class="col_16 groupRow">
			<div class="col_4"><a href="<c:url value="/group/{groupId}"/>">{groupName}</a></div>
			<div class="col_3"><a href="<c:url value="/profile/{adminId}"/>">{adminName}</a></div>
			<div class="col_2 right">{created}</div>
			<div class="col_2 right">{messages}</div>
			<div class="col_2 right" id="members_{groupId}">{members}</div>
			<div class="col_2"><a class='btn {action}Button' href="#" id="joinButton_{groupId}">{joinButtonText}</a></div>
			<div class="col_0">{priv}</div>
		</div>
	</li>
</textarea>

<script type="text/javascript">
var groupFilterLetter;

$(document).ready(function(){
	buildGroupsNav();
    var groupsListTemplate = $.template($('#groupHeaderTemplate').val(), { regx: 'ext' });
    $('#groupsHeaderList').append(groupsListTemplate, { groupNameTitle:'<s:message code="group.title.name"/>', adminNameTitle:'<s:message code="group.title.admin"/>', 
                    createdTitle:'<s:message code="group.title.created"/>', messagesTitle:'<s:message code="group.title.activities"/>', membersTitle:'<s:message code="group.title.members"/>'});
	buildGroupsList();
});

function buildGroupsNav() {
    var groupNavTemplate = $.template($('#groupNavTemplate').val(), { regx: 'ext' });
    $.each(['All', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'], function(i, val) {
        $('#groupsNavList').append(groupNavTemplate, { nav: val});
    });
    $('#groupNav_All').addClass('first');
    $('#groupNav_Z').addClass('last');
    $('.groupNavLetter a').click(refreshGroup);
}

function refreshGroup(event) {
    $('#groupsList').empty();
    buildGroupsList(getId($(this).parent().parent()[0]));
    event.preventDefault();
}

function buildGroupsList(letter) {
    letter = letter||'All';
    groupFilterLetter = letter;
    $.ajax({
        type: 'GET',
        url: url('data/group/list'),
        data: {name: letter=='All'?'':letter},
        dataType: 'json',
        success: function(data){
            var groupsListTemplate = $.template($('#groupRowTemplate').val(), { regx: 'ext' });
            $.each(data.groups, function(i, val) {
                var createdDate = fromISOString(val.created);
                $('#groupsList').append(groupsListTemplate, { groupId: val.id, groupName: val.name, 
                    adminId:val.adminId, adminName:val.adminName + ' ' + val.adminLastName1 + ' ' + val.adminLastName2, 
                    created:createdDate.format('M d'), 
                    messages:val.messagesCount, 
                    members:val.membersCount,
                    joinButtonText: val.currentUserIsMember?leaveText():joinText(),
                    action: val.currentUserIsMember?'leave':'join',
                    priv: val.priv?'<img src="<c:url value="/static/img/lock.png"/>">':''
                });
            });
            $('.joinButton').click(joinGroup);
            $('.leaveButton').click(leaveGroup);
        }
    });
    
}

function joinText() {
    return '<span><span><s:message code="group.joinGroupButtonText"/></span></span>'; 
}

function leaveText() {
    return '<span><span><s:message code="group.leaveGroupButtonText"/></span></span>'; 
}

function joinGroup(event) {
    joinHandler('join', getId(this));
    event.preventDefault();
}

function leaveGroup(event) {
    joinHandler('leave', getId(this));
    event.preventDefault();
}

function joinHandler(joinAction, groupId) {
    $.ajax({
        type: 'POST',
        url: url('data/person/' + joinAction + '/' + groupId),
        dataType: 'json',
        success: function(data){
            var isJoin = (joinAction=='join');
            $('#joinButton_' + groupId)
                .html(isJoin?leaveText():joinText())
                .toggleClass('joinButton')
                .toggleClass('leaveButton')
                .unbind('click').click(isJoin?leaveGroup:joinGroup);
            $('#members_' + groupId).html(''+(parseInt($('#members_' + groupId).html()) + (isJoin?1:-1)));
            $(document).trigger('myGroupChanged');
        }
    });
}
</script>
