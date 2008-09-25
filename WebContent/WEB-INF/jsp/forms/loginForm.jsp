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
		<s:message code="form.login.title"/>
    </h2>
	<form action="<c:url value='processLogin' />" class="form1 col_13" id="loginForm" method="post">
		<div class='col_3'>
			<label class="leftLabel"><s:message code="form.login.username" />:</label>
		</div>
		<div class='col_9'>
			<input id="j_username" name="j_username" type="text" value="" size="52" onkeypress="return entsub(event, this.form);"/>
		</div>

		<div class='col_3'>
			<label class="leftLabel"><s:message code="form.login.password" />:</label>
		</div>
		<div class='col_9'>
		    <input id="j_password" name="j_password" size="30" type="password" value="" onkeypress="return entsub(event, this.form);"/>
			<a href="<c:url value='forgotPassword.htm'/>" class="forgotPassword"><s:message code="form.login.forgotpassword"/></a>
		</div>

		<div class='col_3'>
			&nbsp;
	    </div>
		<div class='col_6'>
			<div class='col_6'>
				<input checked="checked" id="_spring_security_remember_me" name="_spring_security_remember_me" type="checkbox" value="1"/>
				<label for="remember_me" class="rememberMe"><s:message code="form.login.rememberme"/></label>
			</div>
			<div class='col_6'>
				<a class='btn' href="#" id="submit" onclick="$('#loginForm').submit();return false;"><span><span><s:message code="form.button.submit" /></span></span></a>
			</div>
		</div>
	</form>
</div>

<div class='col_2'>
	&nbsp;
</div>
<div class='col_13 bigTopMargin'>
    <h2>
		<s:message code="form.login.signup.title"/>
    </h2>
	<a class="btn" href="<c:url value='signup.htm'/>" id="signup"><span><span><s:message code="form.login.signup.button" /></span></span></a>
</div>

<script type="text/javascript" language="JavaScript">
	document.forms['loginForm'].elements['j_username'].focus();
	function entsub(event, ourform) {
	    if (event && event.which == 13)
	      ourform.submit();
	    else
	      return true;
	}
</script>
