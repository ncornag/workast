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
		<s:message code="form.forgotPassword.title"/>
    </h2>
	<form action="<c:url value='/data/person/password/regeneratePassword' />" class="form1 col_13" id="regeneratePasswordForm" method="post">
		<div class='col_3'>
			<label class="leftLabel"><s:message code="form.forgotPassword.username" />:</label>
		</div>
		<div class='col_9'>
			<input id="username" name="username" type="text" value="" size="52"/>
		</div>

		<div class='col_6'>
			<div class='col_6'>
				<a class='btn' href="#" id="submit" onclick="$('#regeneratePasswordForm').submit();return false;"><span><span><s:message code="form.button.submit" /></span></span></a>
			</div>
		</div>
	</form>
</div>

<div class='col_2'>
	&nbsp;
</div>

<script type="text/javascript" language="JavaScript">
	document.forms['regeneratePasswordForm'].elements['username'].focus();
</script>
<script type="text/javascript">
$(document).ready(function(){
       $('#regeneratePasswordForm').validatorAjaxForm({
              successCallback: function(){
		document.location.href='<c:url value="login.htm"/>';
              }
        },
        {
            rules: {
                username: {required: true}
            }
        });
});
</script>