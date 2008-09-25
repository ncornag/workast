<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<h2><span><s:message code="stream.globalstream.title"/></span></h2>

<!-- 
<div id="profileStreamTimeline" class="timeline-default col_16" style="height: 300px;"></div>

<script type="text/javascript" >
	Timeline_ajax_url="<c:url value="/static/js/timeline/timeline_ajax/simile-ajax-api.js"/>";
	Timeline_urlPrefix="<c:url value="/js/timeline/timeline_js/"/>";       
	Timeline_parameters='bundle=true';
	$(document).ready(function(){
		$(document).bind('streamRefreshed', function(event, params) {
			buildTimeline($('#profileStreamTimeline')[0], params.activities);
		});
	});
</script>
<script src="<c:url value="/static/js/timeline/timeline_js/timeline-api.js"/>"></script>
 -->

<div id="stream" class="globalStream" url="" refreshOnChangeStatus="true"></div>
<script type="text/javascript" src="<c:url value="/js/stream.js.htm"/>"></script>
