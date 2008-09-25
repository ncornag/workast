<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<div class='col_2'>
	&nbsp;
</div>
<div class='col_13'>

	<h3>
		<span><s:message code="group.form.title" /></span>
	</h3>
	<form action="<c:url value="/data/group/new"/>" id="groupForm" name="groupForm" method="post" >
		
		<div class='col_4'>
			<label class="leftLabel"><s:message code="group.form.name" />:</label>
		</div>
		<div class='col_8'>
			<input id="name" name="name" size="50" type="text" value=""/>
		</div>

		<div class='col_4'>
			<a class='btn' href="#" id="submitButton" onclick="$('#groupForm').submit();return false;"><span><span><s:message code="form.button.submit" /></span></span></a>
		</div>
	</form>

</div>

<script type="text/javascript">
$(document).ready(function(){
	
    $('#groupForm').validatorAjaxForm({
            beforeSubmitCallback: function(formData, jQForm, options) {
            },
            successCallback: function(data){
				response = data.response;
				if (response.status=="OK") {
					document.location.href= groupUrl(data.response.data.value);
				} else {
					showResponseErrors(data.response.errors);
				}
			}
        },
        {   
            rules: {
				name: {required: true, minlength: 1, maxlength: 50},
			}
        }
    );

	//var binder = new Binder.FormBinder( $('#groupForm')[0], user );
	//binder.deserialize();

});
</script>