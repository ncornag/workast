<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>

<div id="top">
	<div id="loader" class="loader_balls" style="display:none"></div>
	<div class="accesories" >
		<div>
			<a href="#"><s:message code="nav.help" /></a>
			|
			<a href="<c:url value="/processLogout"/>"><s:message code="nav.logout" /></a>
		</div>
		<div class="search">
			<input id="q" name="personId" size="30" type="text" value="<s:message code="form.search.inputHelp" />">
		</div>
	</div>
</div>

<script type="text/javascript">
function parsePersonsCollection(data) {
    var parsed = [];
    var rows = data.persons;
    for (var i=0; i < rows.length; i++) {
        parsed[parsed.length] = {
            data: rows[i],
            result: ''
        };
    }
    return parsed;
}

function formatPersonCollection(data, i, max, value, term){
    return data.name + ' ' + data.lastName1 + ' ' + data.lastName2;
}

$(document).ready(function(){

    // Person filter input box
    var searchHelpValue = '<s:message code="form.search.inputHelp"/>';
    $('#q').focus(function (event) {
        if (this.value == searchHelpValue) {
            this.value = '';
            $('#q').attr('style', 'color:#333333');
        }
    }).blur(function (event) {
        if (this.value == '') {
            this.value = searchHelpValue;
            $('#q').removeAttr('style');
        }
    });
    
    // Person filter autocomplete
    $("#q").autocomplete('<c:url value="/data/profile/search"/>', {
        dataType: 'json',
        mustMatch: false,
		cacheLength: 0,
        parse: parsePersonsCollection,
        formatItem: formatPersonCollection
    }).result(function() {
        document.location.href='<c:url value="/profile/"/>' + arguments[1].id;
    });

});
</script>

<!-- 
<div class='connections'>
  <a href="#" onclick=""><span><strong><s:message code="topbar.connections" /></strong></span> (<strong id='online_contacts_count'>0</strong> online)</a>
  <div class='contacts_dropdown' id='contacts_dropdown' style='display: none'>
    &nbsp;
  </div>
</div>
 -->