<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<div class="current_status itsme">
	<p>
		<em>
			<s:message code="form.status.currently" />:
		</em>
		<span>
			<a href="#" id="current_status_id" class="current_status_text itsme"></a>
		</span>
		<a class="clearCurrentStatus" href="#">(<s:message code="form.status.clear"/>)</a>
	</p>
</div>

<script type="text/javascript">
function loadCurrentUser(trigger){
    // Get current user data
    $.ajax({
        type: 'GET',
        url: context + 'data/person/current',
        dataType: 'json',
        success: function(data){
            currentUser = data.person;
           	$(document).trigger(trigger, currentUser);
        }
    });
}

$(document).ready(function(){

    // 'currentUserLoaded' event
    $(document).bind('currentUserLoaded', function(event, params) {
        // Show current user
        $('#currentProfileLink').attr('href', url('profile/' + currentUser.id));
        $('#currentProfileImg').attr('src', profileImgUrl(currentUser.id, currentUser.hasPicture));
		//$('#currentProfileImg').attr('title', profileIconTitle({owner:currentUser}));
        // Show current user status
        if ($.exists('#current_status_id') && currentUser.currentActivityTitle) {
            $('.current_status_text.itsme').html(currentUser.currentActivityTitle);
            $('div.current_status').fadeIn();
        }
    });

    // Get current user data
    loadCurrentUser('currentUserLoaded');

});
</script>
