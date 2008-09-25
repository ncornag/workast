<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<div class='col_2'>
	&nbsp;
</div>
<div class='col_13'>

	<h3>
		<span><s:message code="profile.form.title" /></span>
	</h3>
	<form action="<c:url value="/data/person/save"/>" id="profileForm" name="profileForm" method="post" >
		
		<input name="pictureId" id="pictureId" type="hidden" value=""/>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.email" />:</label>
		</div>
		<div class='col_8'>
			<label id="email" >&nbsp;</label>
		</div>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.name" />:</label>
		</div>
		<div class='col_8'>
			<input id="name" name="name" size="50" type="text" value=""/>
		</div>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.lastName1" />:</label>
		</div>
		<div class='col_8'>
			<input name="lastName1" size="50" type="text" value=""/>
		</div>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.lastName2" />:</label>
		</div>
		<div class='col_8'>
			<input name="lastName2" size="50" type="text" value=""/>
		</div>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.profileTitle" />:</label>
		</div>
		<div class='col_8'>
			<input name="title" size="50" type="text" value=""/>
		</div>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.area" />:</label>
		</div>
		<div class='col_8'>
			<input name="area" size="50" type="text" value=""/>
		</div>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.city" />:</label>
		</div>
		<div class='col_8'>
			<input name="city" size="50" type="text" value=""/>
		</div>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.birthday" />:</label>
		</div>
		<div class='col_8'>
			<input id="birthday" name="birthday" size="10" value=""/>
		</div>

		<div class='col_11'>
			<label class="leftLabel"><h4><span><s:message code="profile.form.passwordChange" /></span></h4></label>
		</div>
		<div class="col_11 borderbox">

			<div class='col_4'>
				<label class="leftLabel"><s:message code="profile.form.currentPassword" />:</label>
			</div>
			<div class='col_5'>
				<input id="currentPassword" name="currentPassword" size="20" type="password" value=""/>
			</div>
	
			<div class='col_4'>
				<label class="leftLabel"><s:message code="profile.form.newpassword" />:</label>
			</div>
			<div class='col_5'>
				<input id="password" name="password" size="20" type="password" value=""/>
			</div>
	
			<div class='col_4'>
				<label class="leftLabel"><s:message code="profile.form.passwordAgain" />:</label>
			</div>
			<div class='col_5'>
				<input id="passwordAgain" name="passwordAgain" size="20" type="password" value=""/>
			</div>
		</div>

		<div class='col_4 mb60'>
			<a class='btn' href="#" id="submitButton" onclick="$('#profileForm').submit();return false;"><span><span><s:message code="form.button.submit" /></span></span></a>
		</div>
	</form>

	<div class='col_4 ml160'>
		<label class="leftLabel"><s:message code="profile.form.picture" />:</label>
	</div>
	<div class='col_8'>
		<a id="profilePictureUpload" href="#" class="attachLink"><span><s:message code="profile.form.pictureFile" /></span></a>
		<div id="attachmentLoader" class="loader_circle" style="display:none"></div>
		<img id="uploadedProfilePicture" class="mugshot" src="" />
	</div>

</div>

<script type="text/javascript">
var ppUpload;

$(document).ready(function(){
	
    // 'currentUserLoaded' event
    $(document).bind('currentUserLoaded', function(event, user) {

	    $('#profileForm').validatorAjaxForm({
	            beforeSubmitCallback: function(formData, jQForm, options) {
					if ($("#birthday").datepicker( "getDate" )==null) {
						formData[7] = null;
					} else {
						formData[7].value = $("#birthday").datepicker( "getDate" ).format('Y/m/d');
					}
	            },
	            successCallback: function(){
					if($('#pictureId').val()!="") {
						// Reload the profile picture
						$.ajax({url:profileImgUrl(currentUser.id, true), async:false, cache:true});
					}
					document.location.href='<c:url value="/profile/"/>' + currentUser.id;
	            }
	        },
	        {   
	            rules: {
					name: {required: true, minlength: 1, maxlength: 50},
					lastName1: {required: true, minlength: 1, maxlength: 50},
					lastName2: {required: false, maxlength: 50},
					title: {required: false, maxlength: 50},
					area: {required: false, maxlength: 50},
					city: {required: false, maxlength: 50},
					birthday: {required: false},
					password: {minlength: 5, required: false},
					password2: {equalTo: "#password"}
				}
	        }
	    );

		ppUpload = $('#profilePictureUpload').upload({
			name: 'file',
			method: 'post',
			enctype: 'multipart/form-data',
			action: '<c:url value="/data/fileupload/upload"/>',
			autoSubmit: true,
			onSelect: function() {
				// FIXME: Check extensions
				$('#progress1').text('Selected file ' + ppUpload.filename());
			},
			onSubmit: function() {
			    $('#attachmentLoader').show();
			},
			onComplete: function(data) {
			    $('#attachmentLoader').hide();
				eval('var response=' + data);
				response = response.response;
				if (response.status=="OK") {
				    var data = jsonResponseDataToDictionary(response.data);
					$('#uploadedProfilePicture').attr('src', url('static/files/pending/' + data.get('id')));
					$('#pictureId').val(data.get('id'));
				} else {
					showResponseErrors(response.errors);
				}
			}
		});

	    $('#email').html(user.email);
		$('#uploadedProfilePicture').attr('src', profileImgUrl(currentUser.id, currentUser.hasPicture));

		if (!$.isNull(user.birthday)) {
			var bDate = new Date(user.birthday);
			user.birthday = bDate.format('dataEntry');
		}
		var binder = new Binder.FormBinder( $('#profileForm')[0], user );
		binder.deserialize();

		$("#birthday").datepicker({ 
			changeMonth: true,
			changeYear: true,
			showOn: 'both',
		    buttonImage: '<c:url value="/static/img/calendar.gif"/>', 
			buttonImageOnly: true 
		});

    });
    
});
</script>