<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class='col_2'>
	&nbsp;
</div>
<div class='col_13'>

	<h3>
		<span><s:message code="profile.net.title" /></span>
	</h3>

	<iframe src="<c:url value="/profilegraph/net.htm?personId=${personId}"/>" scrolling="no" class="netGraphFrame"></iframe>

</div>
