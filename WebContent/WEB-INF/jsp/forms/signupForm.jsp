<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>


<div class='col_2'>
	&nbsp;
</div>
<div class='col_13'>
    <h1>
		<s:message code="corp.name"/>
    </h1>
</div>

<div class='col_2'>
	&nbsp;
</div>
<div class='col_13 bigTopMargin'>

	<h2>
		<span><s:message code="signup.form.title" /></span>
	</h2>

	<form action="<c:url value="/data/person/new"/>" id="signupForm" name="signupForm" method="post">

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.email" />:</label>
		</div>
		<div class='col_8'>
			<input id="email" name="email" size="50" type="text" value=""/>
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

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.password" />:</label>
		</div>
		<div class='col_8'>
			<input id="password" name="password" size="20" type="password" value=""/>
		</div>

		<div class='col_4'>
			<label class="leftLabel"><s:message code="profile.form.passwordAgain" />:</label>
		</div>
		<div class='col_8'>
			<input id="passwordAgain" name="passwordAgain" size="20" type="password" value=""/>
		</div>

		<div class='col_4'>
			<a class='btn' href="#" id="submit" onclick="$('#signupForm').submit();return false;"><span><span><s:message code="form.button.submit" /></span></span></a>
		</div>
	</form>
</div>

<script type="text/javascript">
$(document).ready(function(){
	
    $('#signupForm').validatorAjaxForm({
            beforeSubmitCallback: function(formData, jQForm, options) {
				if ($("#birthday").datepicker( "getDate" )==null) {
					formData[7] = null;
				} else {
					formData[7].value = $("#birthday").datepicker( "getDate" ).format('Y/m/d');
				}
            },
            successCallback: function(){
				document.location.href='<c:url value="login.htm"/>';
            }
        },
        {   
            rules: {
				email: {required: true, email: true},
				name: {required: true, minlength: 1, maxlength: 50},
				lastName1: {required: true, minlength: 1, maxlength: 50},
				lastName2: {required: false, maxlength: 50},
				title: 	   {required: false, maxlength: 50},
				area:      {required: false, maxlength: 50},
				city:      {required: false, maxlength: 50},
				birthday:  {required: false},
				password:  {required: true, minlength: 5},
				password2: {required: true, equalTo: "#password"}
			}
        }
    );

    
	$("#birthday").datepicker({ 
		changeMonth: true,
		changeYear: true,
		showOn: 'both',
	    buttonImage: '<c:url value="/static/img/calendar.gif"/>', 
		buttonImageOnly: true 
	});
    
});
</script>
