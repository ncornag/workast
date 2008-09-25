<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<div id="nav">
  <ul>
     <li id="tabmy"><a href="<c:url value="/stream/mystream"/>"><b></b></a></li>
     <li id="taball"><a href="<c:url value="/stream/global"/>"><span></span></a></li>
     <li id="tabprofile"><a href="<c:url value="/profile/0"/>" id="tab-showProfileLink"><span></span></a></li>
     <li id="tabgroups"><a href="<c:url value="/group/list"/>"><span></span></a></li>
  </ul>
</div>

<script type="text/javascript">
$(document).ready(function(){

    // Tab click
	$('#nav>ul>li>a').click(function (event) {
		$(this).parent()
		    .css('margin-left', '15px')
            .css('opacity', 1.0)
		    .siblings()
    		    .css('margin-left', '30px')
    		    .css('opacity', 0.5);
	    });

    // 'currentUserLoaded' event
    $(document).bind('currentUserLoaded', function(event, params) {
        // Rewrite profile tab url
        var id = model.profileId||currentUser.id;
        $('#tab-showProfileLink').attr('href', context + 'profile/' + id);
        
        var selectedTab = $('#nav>ul>li>a')
            .filter(function(index) {
                return uri.fullPath.match(this.pathname);
            });
        if (selectedTab.length==0) {
            selectedTab = $('#nav>ul>li>a')
            .filter(function(index) {
                return uri.fullPath.substring(1, uri.fullPath.lastIndexOf('/')).match(this.pathname.substring(1, this.pathname.lastIndexOf('/')));
            });
        };
        
        $(selectedTab).each(function callback(index) {
            $(this).parent()
                .css('margin-left', '15px')
                .css('opacity', 1.0)
                .addClass('selected');
        });
        $('#nav li:not(.selected)').hover(
                function(){
                    $(this).animate({marginLeft:"15px", opacity: 1.0});
                }
                ,function(){
                    $(this).animate({marginLeft:"30px", opacity: 0.5});
                }
        );
    });

});
</script>

