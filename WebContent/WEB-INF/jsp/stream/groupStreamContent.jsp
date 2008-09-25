<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<h2><span><s:message code="stream.groupstream.title"/></span></h2>

<div id="stream" class="groupStream" url="" refreshOnChangeStatus="true"></div>
<script type="text/javascript" src="<c:url value="/js/stream.js.htm"/>"></script>
