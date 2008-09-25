<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<div class="col_1">
	<h2>
		<a href="#" id="currentProfileLink"><img class="mugshot" id="currentProfileImg"/></a>
	</h2>
</div>
<div id="formTabs" class="col_15 tab-nav">
	<div>
		<h3><s:message code="forms.share"/> <a href="#" id="shareGroupTitle" class="shareGroupTitle"></a>:</h3>
	</div>
    <ul class="tab-nav">
        <li class="selected"><a href="#ft-fragment-1"><span><s:message code="form.status.tabtitle"/></span></a></li>
        <li><a href="#ft-fragment-2"><span><s:message code="form.event.tabtitle"/></span></a></li>
    </ul>
    <div id="ft-fragment-1" class="tab-panel">
		<tiles:insertAttribute name="form1"/>
    </div>
    <div id="ft-fragment-2" class="tab-panel" style="display: none">
		<tiles:insertAttribute name="form2"/>
    </div>
</div>

<script type="text/javascript">
	$(document).ready(function(){

	    // Tab click
		$('#formTabs>ul>li>a').click(function (event) {
			$(this).parent()
			    .addClass('selected')
			    .siblings()
	    		    .removeClass('selected');
		    $('.tab-panel').hide();	
		    $($(this).attr('href')).show();
		    event.preventDefault();
		});

		model.groupId = model.groupId||1;
	    $.ajax({
	        type: 'GET',
	        url: url('data/group/' + model.groupId + '/get'),
	        dataType: 'json',
	        success: function(data){
	            $('#shareGroupTitle').html(data.group.name);
	        }
	    });	

	});
</script>
	
