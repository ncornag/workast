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
		<s:message code="form.recoverPassword.title"/>
    </h2>
	<form action="<c:url value='/data/person/password/saveRecoveredPassword' />" class="form1 col_13" id="saveRecoveredPasswordForm" method="post">
            <c:if test="${uid != null}">
                <input type="hidden" id="uid" name="uid" value="<c:out value='${uid}'/>"/>
            </c:if>
		<div class='col_3'>
			<label class="leftLabel"><s:message code="form.recoverPassword.password" />:</label>
		</div>
		<div class='col_9'>
			<input id="password" name="password" type="password" value="" size="20"/>
		</div>
                <br/>
                <div class="col_3">
                        <label class="leftLabel"><s:message code="form.recoverPassword.repeatPassword"/>:</label>
                </div>
                <div class="col_9">
                        <input id="retypePassword" name="retypePassword" type="password" value="" size="20"/>
                </div>

		<div class='col_6'>
			<div class='col_6'>
				<a class='btn' href="#" id="submit" onclick="$('#saveRecoveredPasswordForm').submit();return false;"><span><span><s:message code="form.button.submit" /></span></span></a>
			</div>
		</div>
	</form>
</div>

<div class='col_2'>
	&nbsp;
</div>

<script type="text/javascript" language="JavaScript">
	document.forms['saveRecoveredPasswordForm'].elements['password'].focus();
</script>
<script type="text/javascript" >
$(document).ready(function() {
       $('#saveRecoveredPasswordForm').validatorAjaxForm({
              successCallback: function(){
		document.location.href='<c:url value="/login.htm"/>';
              }
        },
        {
            rules: {
		password:  {required: true, minlength: 5},
		retypePassword: {required: true, equalTo: "#password"}
            }
        });
});
</script>