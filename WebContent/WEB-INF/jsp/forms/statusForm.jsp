<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<form action="<c:url value="/data/statusactivity/status"/>" id="statusForm" name="statusForm" method="post" >
	<input name="groupId" id="groupId" type="hidden" value="1"/>
	<select id="attachmentIds" name="attachmentIds" multiple="multiple" style="display:none;"></select>
	<div style="float:left;">
		<textarea cols="80" rows="2" name="message" id="message" class="resizable" >${name}</textarea>
	</div>
	<div style="float:left;">
		<div class='col_7'>
			<a class='btn' href="#" id="submit" onclick="$('#statusForm').submit();return false;"><span><span><s:message code="form.button.submit" /></span></span></a>
	        <input id="current" name="current" value="yes" type="checkbox">
	        <label for="status_message_worklog"><s:message code="form.status.addtocurrent" /></label>
		</div>
	</div>
</form>
<div class='col_7'>
	<a id="statusFormUpload" href="#" class="btn" title=""><span><span><s:message code="form.status.attachFile" /></span></span></a>
	<div id="attachmentLoader" class="loader_circle" style="display:none"></div>
	<div id="attachmentProgress"></div>
	<div id="attachmentListDiv"><ul id="attachmentList"></ul></div>
</div>

<textarea id="attachmentDisplay" style="display:none">
	<li class="attachIcon" id="attachment_{id}">
		<div>
			<span>{name} ({size})</span><span onclick="attachmentRemove(this);" class="removeIcon"></span>
		</div>
	</li>
</textarea>

<script type="text/javascript" >

var fUpload;
var attachments = new Dictionary();

function attachmentRemove(element) {
    var id = getId($(element).parent().parent()[0]);
    var attach = $('#attachment_' + id);
    attach.fadeOut("fast", function() {
        attachments.del(id);
        attach.remove();
    	updateAttachmentIds();
    });
}

function updateAttachmentIds() {
    var list = $('#attachmentIds');
    list.empty();
    $.each($('#attachmentList li'), function(i, val) {
	    $('<option selected="selected" value="' + getId(val) + '">id</option>').appendTo(list);
    });                        
}

$(document).ready(function(){
    
	// Resize statusForm text area
	$("#message").markItUp(myWikiSettings);
	// Set forms focus
	$("#statusForm :input:visible:enabled:first").focus();

	$('#statusFormUpload').attr('title', '<s:message code="form.status.attachFile.maxUploadSizeMassage" /> ' + prettyPrintSize('<s:message code="fileManager.attachments.maxUploadSize" />'));

    var attachmentDisplayTemplate = $.template($('#attachmentDisplay').val(), { regx: 'ext' });
	fUpload = $('#statusFormUpload').upload({
		name: 'file',
		method: 'post',
		enctype: 'multipart/form-data',
		action: '<c:url value="/data/fileupload/upload"/>',
		autoSubmit: false,
		fieldName: 'statusAttachment',
		onSelect: function() {
			// Prevent duplicates
			var duplicate = false;
			$.each(attachments.getValues(), function(i, val) {
			    if (val == fUpload.filename()) {
			        duplicate = true;
			    }				
			});
			if (duplicate) {
		        $.modal('<s:message code="form.status.attachDuplicate" />');
			} else {
				fUpload.submit();
			}
			// Check extensions ?
		},
		onSubmit: function() {
			$('#attachmentLoader').show();
			$('#attachmentProgress').text('<s:message code="form.status.attachingFile" /> ' + fUpload.filename());
		},
		onComplete: function(data) {
			$('#attachmentLoader').hide();
			$('#attachmentProgress').empty();
			eval('var response=' + data);
			response = response.response;
			if (response.status=="OK") {
				var data = jsonResponseDataToDictionary(response.data);
				$('#attachmentList').append(attachmentDisplayTemplate, {id: data.get('id'), name: data.get('name'), size: prettyPrintSize(data.get('size'))});
				attachments.put(data.get('id'), data.get('name'));
				updateAttachmentIds();
				$(":file").parent()[0].reset();
			} else {
				showResponseErrors(response.errors);
			}
		}
	});
	fUpload.autoSubmit = false;


	// StatusForm submit
    $('#statusForm').validatorAjaxForm({
            beforeSubmitCallback: function(formData, jQForm, options) {
                formData[0].value = model.groupId;
            },
            successCallback: function(data){
                $(document).trigger('statusChanged', {message: $('#message').val(), current: $('#current')[0].checked} );
                $('#statusForm').resetForm();
                $('iframe.markItUpPreviewFrame').remove();
                attachments = new Dictionary();
                $('#attachmentList').empty();
                updateAttachmentIds();
            }
        },
        {   
            rules: {message: {required: true, minlength: 1, maxlength: 4096} }
        }
    );
		
});
</script>