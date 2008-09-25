<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<h3><span><s:message code="stream.filter.title"/></span></h3>

<div class="filterTypeBar">
	<ul class="filterTypeList">
		<li class="tags">
        	<ul>
				<li class="tag"><a class="onlytag" href="#" code="status"><s:message code="stream.filter.messages"/></a></li>
				<li class="tag"><a class="onlytag" href="#" code="event"><s:message code="stream.filter.events"/></a></li>
			</ul>
		</li>
	</ul>
</div>

<div class="filterTagBar">
	<ul class="filterTagList">
		<li class="searchBox">
			<form action="" name="magicboxsearchform" id="magicboxsearchform" class="on">
				<input type="text" size="25" name="addFilterText" id="addFilterText" class="addFilterText" value="<s:message code="stream.filter.inputTextHelp"/>">
				<input type="submit" id="addFilterTextSubmit" name="addFilterTextSubmit" value="" class="addFilterTextSubmit"/>
			</form>
    	</li>
		<li class="tagBox">
			<form action="" name="magicboxform" id="magicboxform" class="on">
				<input type="text" size="25" name="addFilterTag" id="addFilterTag" class="addFilterTag" value="<s:message code="stream.filter.inputTagHelp"/>">
				<input type="submit" id="addFilterTagSubmit" name="addFilterTagSubmit" value="" class="addFilterTagSubmit"/>
			</form>
		</li>
		<li class="tags">
        	<ul>
			</ul>
		</li>
		<li class="msg">
        	<strong><span class="filterTagMsg"><a href="#" class="refreshStreamLink"><s:message code="stream.refresh"/></a></span></strong>
    	</li>
	</ul>
</div>

