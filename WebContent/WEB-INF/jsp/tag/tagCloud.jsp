<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<h3><span><s:message code="tags.title"/></span></h3>
<div id="myTagsCloud" class="cloud"></div>
<h3><span><s:message code="tags.alltags"/></span><a id="tagsCloudLink" class="withMore" href="#"></a></h3>
<div id="tagsCloud" class="cloud"></div>

<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery.tagcloud-2.js"/>"></script>
<script type="text/javascript">
$(document).ready(function(){

	$('#tagsCloudLink').click(function(event){
		if ($('#tagsCloudLink').hasClass('withLess')) {
			$("#tagsCloud").html('');
		} else {
			buildCloud('<c:url value="/data/tag/cloud"/>', 'tagsCloud');
		}
		$('#tagsCloudLink').toggleClass('withLess');
		event.preventDefault();
	});

    // 'tagChanged' event
	$(document).bind('tagChanged', function(event, params) {
		buildCloud('<c:url value="/data/tag/mycloud"/>', 'myTagsCloud');
	});    
	$(document).trigger('tagChanged');
	
});

// Add stream tag filter
function addTagToStreamFilterFromCloud(event) {
	var isGlobal = $(this).parent().attr('id') == 'tagsCloud';
    addTagToStreamFilter($(this).html(), isGlobal);
	if(isGlobal) {
		$('ul.filterTagList li.tags a').addClass('globalTag');
	} else {
		$('ul.filterTagList li.tags a').removeClass('globalTag');
	}
    event.preventDefault();
}

// Make ajax request and build cloud
function buildCloud(url, id) {
	$.ajax({
		type: 'GET',
		url: url,
		dataType: 'json',
		success: function(data){
			$('#' + id).tagCloud(data.cluod, {maxFontSizeEm:2.5});
			if($.exists($('#stream'))) {
	  			$('.tagcloudlink').click(addTagToStreamFilterFromCloud);
			}
		}
	});

}
</script>
