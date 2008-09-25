<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="profileInfoPic">
  <img id="profileImg" src="" class="mugshot big" />
</div>

<div class="profileInfo">
	<h2 id="profileName"></h2>
	<h3 id="profileTitle" class="title"></h3>
	<h3 id="profileLocation" class="dept"></h3>
	<p class="current_status">
		<em>
		  <s:message code="profile.currently" />:
		</em>
		<span>
		  <a href="#" id="currentActivityInProfile" class="current_status_text"></a>
		</span>
	</p>
</div>

<div class="profileBox">
	<a href="#" id="profileFollowButton" class="btn followButton"></a>
	<a href="<c:url value="/profile/edit"/>" id="profileEditButton" class="btn profileEditButton"><span><span><s:message code="profile.editprofile" /></span></span></a>
</div>

<div class="profileBox">
	<h2>
		<s:message code="profile.connections" />
	</h2>
	<div class="col_7">
		<h4>
			<a id="profileFollowingLink" href="#" ><s:message code="profile.following"/> <span id="profileFollowingCount"></span></a>
		</h4>
		<ul>
			<li id="profileFollowingList" class="profileFollowerList"></li>
		</ul>
	</div>
	<div class="col_7">
		<h4>
			<a id="profileFollowersLink" href="#" ><s:message code="profile.followers"/> <span id="profileFollowersCount"></span></a>
		</h4>
		<ul>
			<li id="profileFollowersList" class="profileFollowerList"></li>
		</ul>
	</div>
</div>

<div class="profileBox">
	<h2>
	  <s:message code="profile.stats"/>
	</h2>
	<ul>
		<li>
			<div class="statType">
				<s:message code="profile.activitiesStat"/> (<span id="actCount"></span>)
			</div>
			<div class="statSparkline">
				<div class="statSparklineRow">
					<span id="actSparkline">Loading..</span>
				</div>
				<div class="statSparklineRow">
					<span class="actSparklineLabel">14 days activities count</span>
				</div>
			</div>
		</li>
	</ul>
</div>

<div class="profileBox">
	<h2><s:message code="profile.contact"/></h2>
	<div class="vcard">
		<div class="fn" id="profileFullName"></div>
		<div class="email" id="profileEMail"></div>
<!-- 
		<div class="tel mobile">
			<span class="type">Cell:</span>
			<span class="value">555 555 5555</span>
		</div>
		<div class="tel office">
			<span class="type">Work:</span>
			<span class="value">
			  555 555 5555
			</span>
		</div>
		<div class="url aim"><a href="aim:goim?screenname=testtime">testtime</a></div>
		<div class="url msn">test@msn.com</div>
		<div class="url google_talk"><a href="gtalk:chat?jid=test@workast.com">test@demo.workast.com</a></div>
		<div class="url yahoo"><a href="ymsgr:sendim?testtimes12">testtimes12</a></div>
		<div class="url skype"><a href="skype:test_james?call">test_james</a></div>
		<div class="url jabber"><a href="xmpp:test@workast.jabber.com">test@workast.jabber.com</a></div>
 -->
	</div>
</div>

<script type="text/javascript">
var profileUser;

$(document).ready(function(){
	
	$(document).bind('updateFollowers', function(event, params) {
		updateFollowPersons('profileFollowersLink', 'followers', 'profileFollowersCount', 'profileFollowersList', true);
	});

    // 'currentUserLoaded' event
    $(document).bind('currentUserLoaded', function(event, params) {
		$('#stream').attr('url', url('data/stream/profile/' + model.profileId ));
		$(document).trigger('refreshStream', {id: getFirstStreamId()});
		
	    // Get profile user data
	    $.ajax({
	        type: 'GET',
	        url: url('data/person/' + model.profileId + '/get'),
	        dataType: 'json',
	        success: function(data){
	            profileUser = data.person;
	            $('#profileImg').attr('src', profileImgUrl(profileUser.id, profileUser.hasPicture));
				$('#profileImg').attr('title', profileIconTitle({owner:profileUser}));
				$('#profileName').html(profileUser.name + ' ' + (profileUser.lastName1?profileUser.lastName1:'') + (profileUser.lastName2?' ' + profileUser.lastName2:'') );
				$('#profileTitle').html((profileUser.title?profileUser.title:'') + (profileUser.area?' - ' +profileUser.area:''));
				$('#profileLocation').html(profileUser.city?profileUser.city:'');
				$('#profileFullName').html(profileUser.name + ' ' + profileUser.lastName1 + ' ' + profileUser.lastName2 );
				$('#profileEMail').html('<a href="mailto:' + profileUser.email + '"> '+ profileUser.email +'</a>');
				if(profileUser.currentActivityTitle) {
					$('#currentActivityInProfile').html(profileUser.currentActivityTitle);
					$('div.profileInfo .current_status').show()
				}
			    if (profileUser.id==currentUser.id) {
			    	// Show current status in profile
			        $('div.profileInfo .current_status').addClass('itsme');
			        $('#currentActivityInProfile').addClass('itsme');
			        // Show edit profile button
			        $('#profileEditButton').show();
			    }
			    // Follow
			    updateFollowPersons('profileFollowingLink', 'following', 'profileFollowingCount', 'profileFollowingList');
			    updateFollowPersons('profileFollowersLink', 'followers', 'profileFollowersCount', 'profileFollowersList', true);

				$(document).trigger('profileUserLoaded', profileUser);
        
	        }
	    });
	
        $.ajax({
            type: 'GET',
            url: url('data/person/' + model.profileId + '/statistics'),
            dataType: 'json',
            success: function(data){
                $('#actSparkline').sparkline(data.personStatistic.activityCount, {type:'line', width:100});
                var count=0;
                $.each(data.personStatistic.activityCount, function(i, val){count+=val;});
                $('#actCount').html(count);
            }
        });

    });
    
});

var isFollower;
function updateFollowPersons(linkName, following, countName, followList, updateButton) {
	isFollower=false;
    $('#' + linkName).attr('href', url('profile/' + model.profileId + '/net'));
    $.ajax({
        type: 'GET',
        url: url('data/person/' + model.profileId + '/' + following),
        dataType: 'json',
        success: function(data){
			$(document).trigger(following + 'Loaded', data);
            var follow = data.persons;
            $('#' + countName).html('(' + follow.length + ')');
            $('#' + followList).empty();
            $.each(follow, function(i, val) {
                $('#' + followList).append('<a href="' + url('profile/' + val.id) +'"><img class="mugshot small" src="' + profileImgUrl(val.id, val.hasPicture) + '" title="' + profileIconTitle({owner:val}) + '"/></a>');
                if (val.id==currentUser.id) isFollower=true;
            });
            if (updateButton && (profileUser.id!=currentUser.id)) {
                // Follow/Unfollow
                if (isFollower) {
                    $('#profileFollowButton').show().html(unfollowText()).unbind('click').click(unfollowPerson);
                } else {
                    $('#profileFollowButton').show().html(followText()).unbind('click').click(followPerson);
                }
            }
        }
    });
}

function unfollowText() {
    return '<span><span><s:message code="profile.unfollowthisperson" /></span></span>'; 
}

function followText() {
    return '<span><span><s:message code="profile.followthisperson" /></span></span>'; 
}

function unfollowPerson(event) {
    followHandler('unfollow');
    event.preventDefault();
}

function followPerson(event) {
    followHandler('follow');
    event.preventDefault();
}

function followHandler(followAction) {
    $.ajax({
        type: 'POST',
        url: url('data/person/' + followAction + '/' + profileUser.id),
        dataType: 'json',
        success: function(data){
            $(document).trigger('updateFollowers');
        }
    });
}
</script>
