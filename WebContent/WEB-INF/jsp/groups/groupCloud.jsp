<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<h3><span><s:message code="mygroups.title"/></span></h3>
<div id="groupcloud" class="cloud"><ul id="myGroupsList"></ul></div>

<script type="text/javascript">
$(document).ready(function(){

    // 'groupChanged' event
	$(document).bind('myGroupChanged', function(event, params) {
		$('#myGroupsList').empty();
	    $.ajax({
			type: 'GET',
			url: '<c:url value="/data/person/current/groups"/>',
			dataType: 'json',
			success: function(data){
				var groupsListTemplate = $.template($('#myGroupRowTemplate').val(), { regx: 'ext' });
				$.each(data.groups, function(i, val) {
					$('#myGroupsList').append(groupsListTemplate, { groupId: val.id, groupName: val.name});
				});

				var actualGroup = $('#shareGroupTitle').html();
				if (actualGroup!=null) {
		            $('#myGroupsList>li>div>div>a')
		            	.filter(function(index) {
		                	return actualGroup.match($(this).html());
		            	}).parent().parent().addClass('selected');
				}           	
			}
		});
	});    
	$(document).trigger('myGroupChanged');
	
});
</script>

<textarea id='myGroupRowTemplate' style='display:none'>
	<li>
		<div class="col_6 myGroupRow">
			<div class="col_6"><a href="<c:url value="/group/{groupId}"/>">{groupName}</a></div>
		</div>
	</li>
</textarea>