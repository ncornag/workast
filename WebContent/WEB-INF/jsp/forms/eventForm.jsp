<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<form action="<c:url value="/data/eventactivity/event"/>" id="eventForm" name="eventForm" method="post">
    <input name="groupId" id="groupId" type="hidden" value="1"/>
	<div class="col_15 mb05">
		<label><s:message code="form.event.title" /> </label>
		<input type="text" size="30" value="" name="title" id="title"/>
		<label><s:message code="form.event.from" />:</label>
		<input type="text" size="10" value="" id="startDate" name="startDate"/> <s:message code="form.event.to" /> <input type="text" size="10" value="" id="endDate" name="endDate"/> 
	</div>
	<div class="col_15">
		<div class='col_2'>
			<a class='btn' href="#" id="submitButton" onclick="$('#eventForm').submit();return false;"><span><span><s:message code="form.button.submit" /></span></span></a>
		</div>
		<h3><span><b>Not yet fully implemented</b></span></h3>
	</div>
</form>

<script type="text/javascript" >
$(document).ready(function(){
	$("#startDate,#endDate").datepicker({ 
	    beforeShow: customRange, 
	    showOn: "both", 
	    buttonImage: '<c:url value="/static/img/calendar.gif"/>', 
	    buttonImageOnly: true 
	});
	
    $('#eventForm').validatorAjaxForm({
	        beforeSubmitCallback: function(formData, jQForm, options) {
    			formData[0].value = model.groupId;
				if ($("#startDate").datepicker( "getDate" )==null) {
					formData[2] = null;
				} else {
					formData[2].value = $("#startDate").datepicker( "getDate" ).format('Y/m/d');
				}
				if ($("#endDate").datepicker( "getDate" )==null) {
					formData[3] = null;
				} else {
					formData[3].value = $("#endDate").datepicker( "getDate" ).format('Y/m/d');
				}
	        },
	        successCallback: function(){
                $(document).trigger('statusChanged', {} );
                $('#eventForm').resetForm();
	        }
	    },
	    {   
	        rules: {
				title: {required: true, minlength: 1, maxlength: 50},
				startDate: {required: true},
				endDate: {required: false}
			}
	    }
	);
	
});
function customRange(input) { 
    return {minDate: (input.id == "endDate" ? $("#startDate").datepicker("getDate") : null), 
        maxDate: (input.id == "startDate" ? $("#endDate").datepicker("getDate") : null)}; 
} 
</script> 
