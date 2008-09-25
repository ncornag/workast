<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

var tagsFilter = new Dictionary();

var streamTemplate;
var commentTemplate;
var tagsEditTemplate;
var tagsTemplate;
var attachmentTemplate;
var attachmentTemplateAsImage;

var pollInterval = 60000;
var pollIntervalId;

var hasFocus;

// Call REST url
function refreshStream(params){
    if ($('#stream').attr('url')!='') {
        auto = params.auto||false;
        backwards = params.backwards||false;
        globalTags = params.globalTags||false;
        lastGlobalTags = globalTags;
        maxResults = params.maxResults||20;
        type = params.type||model.type||'';
        text = params.text||'';
        text = text=='<s:message code="stream.filter.inputTextHelp"/>'?'':text;
        data = {firstId:params.id, type:type, maxResults:maxResults, backwards:backwards, tags: tagsFilter.getValues(), globalTags: globalTags, text: text, auto: auto};
    	$.ajax({
    		type: 'GET',
    		url: $('#stream').attr('url'),
    		data: data,
    		dataType: 'json',
    		contentType: 'application/json',
    		success: loadStreamTable
    	});
    }
}

// Render stream table
var loadStreamTable = function(data) {
	//console.log(data);	
    clearInterval(pollIntervalId);    
    pollInterval = data.container.pollInterval;
    pollIntervalId = setInterval(pollTheStream, pollInterval);    
	var today = new Date();
	var activities = data.container.activities.activity;

	// Find where to put the list
	var atFirst = false;
	var streamList = $('#streamList');
	if (streamList.length==0) {
		// Insert activities list
		streamList = $('#stream').append("<ul id='streamList' class='stream'></ul>");
	} else {
		firstIdInPlace = getFirstStreamId();
		if (activities.length>0) {
			atFirst = (firstIdInPlace < activities[0].id);
		}
	}
	
	// Add activities, reuse elements to see a smoth transition
	for(var a = 0; a < activities.length;a++) {
		var act = activities[a];
		var comments = act.comments.activity;
		for (v in act.data) {
			act['data_' + v] = act.data[v];
		}
        if (act.type=='EVENT') { //TODO: Generalize
            act.startDate = fromISOString(act.data.startDate);
            act.endDate = fromISOString(act.data.endDate);
            act.startDateFormatted = act.startDate.format(Date.formats[act.startDate.getHours()==1?'full1':'full']);
            act.endDateFormatted = act.endDate.format(Date.formats[act.endDate.getHours()==1?'full1':'full']);
		}
		var activityDate = fromISOString(act.postedTime);
		var fullPostedTime = activityDate.format(Date.formats[activityDate.getHours()==1?'full1':'full']);
		if (!$.exists('#conversation_piece_' + act.id)) {
			if (act.owner.id==currentUser.id && act.parentOwnerId==currentUser.id) {
				act.parentOwnerName = '<s:message code="stream.yourself"/>';
			} else if (act.parentOwnerId==currentUser.id) {
				act.parentOwnerName = '<s:message code="stream.you"/>';
			}
			
			var groupInShow = act.group.id==1?'none':'inline';
			act.context = context;
			if (act.replyToRenderedMessage) {
				act.replyToRenderedMessage = encodeToTooltip(act.replyToRenderedMessage);
			}
			vars = { id: act.id, 
			         activityIconClass: act.type + '-ActivityIcon', 
			         iconTitle: profileIconTitle(act),
			         message: $.tmpl(activityMessageTemplates[act.type], act),
			         prettyPostedTime: activityDate.pretty(today), 
			         fullPostedTime: fullPostedTime,
			         timeData: act.postedTime,
			         groupInShow: groupInShow,
			         groupId: act.group.id,
			         groupName: act.group.name,
			         ownerName: act.owner.id==currentUser.id ? '<s:message code="stream.you"/>' : act.owner.name,
			         ownerPicture: act.owner.hasPicture?act.owner.id:'generic',
			         ownerId: act.owner.id
			};
			if (atFirst && firstIdInPlace) {
				$('#conversation_piece_' + firstIdInPlace).before(streamTemplate, vars);
			} else {
				$('#streamList').append(streamTemplate, vars);
			}

			// Bind commentsFormAction
			$('#commentReplyAction_' + act.id).click(function(event){
				showHideCommentForm(this, false);
				event.preventDefault();
			});
			
			// Bind commentForm submit
			$('#commentForm_' + act.id).bind('submit', function(){
				var id = getId(this); 
				$(this).ajaxSubmit({
					success: function() {
						document.forms['commentForm_' + id].reset();
						showHideCommentForm($('#commentReplyAction_' + id)[0], false);
						$(document).trigger('refreshStream', {id: id-1});
					}
				});
				return false;
			});
		}

		// Insert Attachments
		if(act.data && act.data.attachments) {
			var attachments = act.data.attachments;
			if (attachments.length!=0) {
				if ($('#actAttachmentList_' + act.id).children().length==0) {
					$.each(attachments, function(i, val) {
						if (val.contentType && val.contentType.substr(0,5)=='image') {
							$('#actAttachmentList_' + act.id).append(attachmentTemplateAsImage, { actId: act.id, id: val.id, name:val.name, size:prettyPrintSize(val.size) });
						} else {
							$('#actAttachmentList_' + act.id).append(attachmentTemplate, { actId: act.id, id: val.id, name:val.name, size:prettyPrintSize(val.size) });
						}
					});
				}
			}
			$('#actAttachmentListDiv_' + act.id).show();
		}
		
		// Insert Comments
		if (comments.length!=0) {
			$('#commentsDisplayLink_' + act.id).text(comments.length + ' <s:message code="stream.comments"/>');
			// Bind commentsFormAction
			$('#commentsDisplayLink_' + act.id).unbind().click(function(event){
				if (!$(this).hasClass('openThread')) {
					$(this).addClass('openThread');
					$('#commentsDiv_' + getId(this)).show();
				} else {
					$(this).removeClass('openThread');
					$('#commentsDiv_' + getId(this)).hide();
				}
				event.preventDefault();
			});
			$('#commentsDisplayDiv_' + act.id).show();
			for(var c = 0; c < comments.length;c++) {
				com = comments[c];
				var commentDate = fromISOString(com.postedTime);
				var commentFullPostedTime = commentDate.format(Date.formats[commentDate.getHours()==1?'full1':'full']);
				if(!$.exists('#comment_' + com.id)) {
					if (com.owner.id==currentUser.id && com.replyToOwnerId==currentUser.id) {
						com.replyToOwnerName = '<s:message code="stream.yourself"/>';
					} else if (com.replyToOwnerId==currentUser.id) {
						com.replyToOwnerName = '<s:message code="stream.you"/>';
					}
					
			        com.context = context;
					if (com.replyToRenderedMessage) {
						com.replyToRenderedMessage = encodeToTooltip(com.replyToRenderedMessage);
					}
					$('#commentsList_' + act.id).append(commentTemplate, { id: com.id, 
					                                                       message: $.tmpl(activityMessageTemplates['COMMENT'], com),
					                                                       commenterPicture: com.owner.hasPicture?com.owner.id:'generic',
					                                                       commenterId: com.owner.id,
					                                                       iconTitle: profileIconTitle(com),
					                                                       commenter: com.owner.id==currentUser.id ? '<s:message code="stream.you"/>' : com.owner.name, 
					                                                       fullPostedTime: commentFullPostedTime,
					                                                       timeData: com.postedTime, 
					                                                       prettyPostedTime: commentDate.pretty(today)
					                                                     });
				}
			}
		}
		
        // Insert Tags
		var tags = act.activitytags.tag;
        if (tags.length!=0) {
            for(var c = 0; c < tags.length;c++) {
                tag = tags[c];
                addTagToActivityChain(act.id, tag.name);
                addTagToActivityEdit(act.id, tag.name, false);
            }
            $('#showTagsList_' + act.id).show();
        }
        
    }
	
    // Stream tag filter add
    $('.activityTagLink').click(addTagToStreamFilterFromActivity);
    
    // Stream tag filter remove
    $('.filterTagBar .onlytag').click(removeTagFromStreamFilter);
    
    // Stream type filter add
    $('.filterTypeBar .onlytag').click(addTypeToStreamFilter);
    
    // Activity tag button
    $('a.addTag').click(showEditActivityTags);

    // Activity tag add
    $('.editTagForm').validatorAjaxForm({
            successCallback: addTagToActivityHandler
        },        
        {   focusCleanup: true,
            rules: {
                name: {
                    required: true,
                    lettersonly: true,
                    minlength: 1
                }
            }
        }
    );    
    
    // Activity tag delete
    $('.editTagsDiv .removetag').click(removeTagFromActivityHandler);
    
    // Activity tag autocomplete
    /* Can't change the url
     */
    $(".addtagEdit").autocomplete('<c:url value="/data/person/tags/mytags"/>', {
        dataType: 'json',
        mustMatch: false,
        cacheLength: 0,
        parse: parseTagsCollection
    });
    
    // Show new activities
    $(document).trigger('streamRefreshed', {activities:activities});
    
}

function addTagToActivityHandler(response, jQForm){
    var responseData = [].concat(response.response.data||[]);
    if (responseData.length==0) {
        var id = getId(jQForm[0]);
        var name = $('#magicboxform_' + id + ' :input:first').val();
        addTagToActivityChain(id, name);
        addTagToActivityEdit(id, name, true);                    
        var name = $('#magicboxform_' + id + ' :input:first').val('');
		$('#addTag_' + id).click();
        $(document).trigger('tagChanged');
    }
}

function addTagToActivityChain(activityId, tagName) {
    if (!$.exists('#showTagsList_' + activityId)) {
        $('#showTagsDivPlaceholder_' + activityId).replaceWith("<ul id='showTagsList_" + activityId + "', class='activityTag' style='display:none;'></ul>");
    }
    if(!$.exists('#tag_' + activityId + '_' + tagName)) {
        $('#showTagsList_' + activityId).append(tagsTemplate, { id: activityId, name: tagName});
        $('.activityTagLink').click(addTagToStreamFilterFromActivity);
    }
}

function addTagToActivityEdit(activityId, tagName, show) {
    if(!$.exists('#editTag_' + activityId + '_' + tagName)) {
        $('#editTagsList_' + activityId).append(tagsEditTemplate, { id: activityId, name: tagName});
        if (show) $('#editTag_' + activityId + '_' + tagName)
                    .fadeIn("fast")
                    .click(removeTagFromActivityHandler);
    }
}

function removeTagFromActivityChain(activityId, tagName) {
    $('#tag_' + activityId + '_' + tagName).remove();
}

function removeTagFromActivityEdit(activityId, tagName) {
    var element = $('#editTag_' + activityId + '_' + tagName); 
    $(element).fadeOut("fast", function() {
        $(element).remove();
    })
}

function removeTagFromActivityHandler(event) {
    var parent = $(this).parent();
    var id = getId(this);
    var name = getId(this, 2);
    var url = $.template('<c:url value="/data/activity/{id}/tag/{name}"/>', { regx: 'ext' }).apply({id:id, name:name});
    $.ajax({
        type: 'DELETE',
        url: url,
        success: function(msg){
            removeTagFromActivityEdit(id, name);
            removeTagFromActivityChain(id, name);
            $(document).trigger('tagChanged');
        }
      });
}

function showEditActivityTags(event) {
    var id = getId(this);
    $('#editTagsDiv_' + id).toggle();
    $('#showTagsList_' + id).toggle();
    $('#magicboxform_' + id + ' :input:first').focus();
    event.preventDefault();
}

// Add stream tag filter
function addTagToStreamFilterFromActivity(event) {
    addTagToStreamFilter($(this).children('.activityTagName').html());
    event.preventDefault();
}

function addTagToStreamFilter(name, globalTags) {
    globalTags = globalTags||false;
    var doRefresh=false;
    $.trim(name);
    if(name!='' && tagsFilter.get(name)==null){
        tagsFilter.put(name, name);
        var tagsFilterTemplate = $.template($('#tagsFilterTemplate').val(), { regx: 'ext' });
        $('.filterTagBar li.tags ul').append(tagsFilterTemplate, { name: name});
        $('#filterTag_' + name).fadeIn("fast");
        $('.filterTagBar .removetag').click(removeTagFromStreamFilter);
        doRefresh = true;
    } else {
        if(name!='' && tagsFilter.get(name)!=null && globalTags!=lastGlobalTags){
            doRefresh = true;
        }
    }
    if (doRefresh){
        $('#stream').empty();
        $(document).trigger('refreshStream', {id: getFirstStreamId(), tags: tagsFilter, text: $('#addFilterText').val(), globalTags: globalTags});
    }
}

// Remove stream tag filter
function removeTagFromStreamFilter(event) {
    var parent = $(this).parent();
    tagsFilter.del(getId(parent[0]));
    parent.fadeOut("fast",function(){
        parent.remove();
        $('#stream').empty();
        $(document).trigger('refreshStream', {id: getFirstStreamId(), tags: tagsFilter, text: $('#addFilterText').val(), globalTags: lastGlobalTags});
    });
    event.preventDefault();
};

// Add stream type filter
function addTypeToStreamFilter(event) {
    var type = $(this).attr("code");
    $(this).parent()
                .siblings().removeClass('on')
                    .children().removeClass('on').end()
                .end()
                .toggleClass('on')
            .end()
            .siblings().toggleClass('on').end()
            .toggleClass('on');
    $('#stream').empty();
    model.type = model.type==type?'':type;
    $(document).trigger('refreshStream', {id: getFirstStreamId(), tags: tagsFilter, text: $('#addFilterText').val()});
    event.preventDefault();
};

// Show/Hide comments form
function showHideCommentForm(element, detail) {
    var id = getId(element);
    if (detail) {
        $('#commentReplyAction_' + id).removeClass('show');
    } else {
        $('#viewDetailsAction_' + id).removeClass('show').text('<s:message code="stream.detail.view"/>');
    }
    $(element).toggleClass('show');
    var visible = $(element).hasClass('show');
    if (visible) {
        $('#comment_form_' + id).fadeIn().find('#comment').focus();
        $('#action_container_' + id).fadeIn();
        $('#commentForm_' + id + ' :input:first').focus();
        if (detail) $('#details_' + id).fadeIn(); else $('#details_' + id).hide();
    } else {
        $('#action_container_' + id).hide();
        $('#comment_form_' + id).hide();
        $('#details_' + id).hide();
    }    
    if (detail) $(element).text(visible ? '<s:message code="stream.detail.hide"/>' : '<s:message code="stream.detail.view"/>');
}

function parseTagsCollection(data) {
    var parsed = [];
    var rows = [].concat(data.tags||[]);
    for (var i=0; i < rows.length; i++) {
        parsed[parsed.length] = {
            data: [].concat(rows[i].name),
            value: rows[i],
            result: rows[i].name
        };
    }
    return parsed;
}

function showThread(event) {
    activityId = getId(this);
    UnTip();
    $('#stream').empty();
    $(document).trigger('refreshStream', {id: activityId, maxResults:1, backwards:true});
    event.preventDefault();
}

function pollTheStream() {
    $(document).trigger('refreshStream', {id: getFirstStreamId(), text: $('#addFilterText').val(), auto: true});
}

function buildTimeline(element, activities) {
	if (activities && activities.length>0) {
	    var eventSource = new Timeline.DefaultEventSource(0);
	    
	    var focusDate = Timeline.DateTime.parseGregorianDateTime(activities[0].postedTime);
	    var bandInfos = [
			Timeline.createBandInfo({
				eventSource:    eventSource,
				date:           focusDate,
				width:          "70%", 
				intervalUnit:   Timeline.DateTime.HOUR, 
				intervalPixels: 100
			}),
			Timeline.createBandInfo({
				overview:       true,
				eventSource:    eventSource,
				date:           focusDate,
				width:          "20%", 
				intervalUnit:   Timeline.DateTime.DAY, 
				intervalPixels: 200
			}),
			Timeline.createBandInfo({
				overview:       true,
				eventSource:    eventSource,
				date:           focusDate, 
				width:          "10%", 
				intervalUnit:   Timeline.DateTime.MONTH, 
				intervalPixels: 400
			})                     
	    ];
	    
	 	bandInfos[1].syncWith = 0;
	   	bandInfos[1].highlight = true;
	    
	   	bandInfos[2].syncWith = 0;
	   	bandInfos[2].highlight = true;
	   	
	    var tl = Timeline.create(element, bandInfos, Timeline.HORIZONTAL);
	    
	    var eventsContainer = {events: []};
	    var i=0;
		for(var a = 0; a < activities.length;a++) {
			var act = activities[a];

            if (act.type=='EVENT') {
                params = act;
                params.context = context;
                message = $.tmpl(activityMessageTemplates[act.type], params);
                eventsContainer.events[i++] = {
                    start: act.startDate,
                    end: act.endDate,
                    isDuration: false,
                    title: (act.owner.id==currentUser.id ? '<s:message code="stream.you"/>' : act.owner.name) + ' ' + message,
                    description: (act.owner.id==currentUser.id ? '<s:message code="stream.you"/>' : act.owner.name) + ' ' + message,
                    icon: '<c:url value="/static/img/iconEvent.png"/>'
                };
            } else {
                params = act;
                params.context = context;
                message = $.tmpl(activityMessageTemplates[act.type], params);
                eventsContainer.events[i++] = {
                    start: act.postedTime,
                    title: (act.owner.id==currentUser.id ? '<s:message code="stream.you"/>' : act.owner.name) + ' ' + message,
                    description: (act.owner.id==currentUser.id ? '<s:message code="stream.you"/>' : act.owner.name) + ' ' + message,
                    icon: act.type=='STATUS'?'<c:url value="/static/img/iconStatus.png"/>':'<c:url value="/static/img/iconComment.png"/>'
                };
                
            }

		}
		
	    eventSource.loadJSON(eventsContainer, "test");
	    //    tl.loadJSON("/static/js/timeline/example.js?"+ (new Date().getTime()), function(json, url) {
	    //        eventSource.loadJSON(json, url);
	    //    });
	}
}


/* jQuery onLoad */ 
$(document).ready(function(){

	$(document).bind('currentUserLoadedSimple', function(event, params) {
	    $('.current_status_text.itsme').html(currentUser.currentActivityTitle);
	    $('.current_status.itsme').fadeIn();
	});
	
	// 'statusChanged' event
	$(document).bind('statusChanged', function(event, params) {
	    if (params.current) {
	        loadCurrentUser('currentUserLoadedSimple');
	    }
	    if($.exists($('#stream')) && $('#stream').attr('refreshOnChangeStatus')=='true') {
	        $(document).trigger('refreshStream', {id: getFirstStreamId()});
	    }
	});
	
	// 'refreshStream' event
	$(document).bind('refreshStream', function(event, params) {
		refreshStream(params);
	});
	
	// Refresh times
	setInterval(function(){ $(".prettyTime").prettyDate(); }, 60000);

	// Clear status link
	$('a.clearCurrentStatus').click(function(event){
		$.post('<c:url value="/data/statusactivity/clearstatus"/>', {}, function(){
			$('.current_status_text.itsme').fadeOut();
		});
		event.preventDefault();
	});

	// Refresh stream links
	$('.refreshStreamLink').click(function(event){
		$(document).trigger('refreshStream', {id: getFirstStreamId(), text: $('#addFilterText').val()});
		event.preventDefault();
	});
	
	$('.moreStreamLink').click(function(event){
		$(document).trigger('refreshStream', {id: getLastStreamId(), backwards: true, text: $('#addFilterText').val()});
		event.preventDefault();
	});

	// LinkForm submit
	$('#linkForm').ajaxForm({
		success: function() {
			self.close();
		}
	});
    $('#linkForm').validatorAjaxForm({
            beforeSubmitCallback: function(formData, jQForm, options) {
                formData[0].value = model.groupId;
            },
            successCallback: function(){
                if($.exists($('#stream')) && $('#stream').attr('refreshOnChangeStatus')=='true') {
                    $(document).trigger('refreshStream', {id: getFirstStreamId()});
                }
                $('#linkForm').resetForm();
            }
        },
        {   rules: {
                title: {required: true, minlength: 3},
                url: {required: true, url: true}
            }
        }
    );

    // Build templates
    streamTemplate = $.template($('#streamTemplate').val(), { regx: 'ext' });
    commentTemplate = $.template($('#commentTemplate').val(), { regx: 'ext' });
    tagsEditTemplate = $.template($('#tagsEditTemplate').val(), { regx: 'ext' });
    tagsTemplate = $.template($('#tagsTemplate').val(), { regx: 'ext' });
    attachmentTemplate = $.template($('#activityAttachmentDisplay').val(), { regx: 'ext' });
    attachmentTemplateAsImage = $.template($('#activityAttachmentDisplayAsImage').val(), { regx: 'ext' });

    // Stream Tag filter input box
    var addTagHelpValue = '<s:message code="stream.filter.inputTagHelp"/>';
    $('#addFilterTag').focus(function (event) {
        if (this.value == addTagHelpValue) {
            this.value = '';
            $('#addFilterTag').attr('style', 'color:#333333');
            $('#addFilterTagSubmit').show();
        }
    }).blur(function (event) {
        if (this.value == '') {
            this.value = addTagHelpValue;
            $('#addFilterTag').removeAttr('style');
            $('#addFilterTagSubmit').hide();
        }
    });
    var addTextHelpValue = '<s:message code="stream.filter.inputTextHelp"/>';
    $('#addFilterText').focus(function (event) {
        if (this.value == addTextHelpValue) {
            this.value = '';
            $('#addFilterText').attr('style', 'color:#333333');
        }
    }).blur(function (event) {
        if (this.value == '') {
            this.value = addTextHelpValue;
            $('#addFilterText').removeAttr('style');
        }
    });
    
    // Stream tag filter autocomplete
    $("#addFilterTag").autocomplete('<c:url value="/data/person/tags/mytags"/>', {
        dataType: 'json',
        mustMatch: true,
        cacheLength: 0,
        parse: parseTagsCollection
    }).result(function(event) {
        addTagToStreamFilter($('#addFilterTag').val());
        $('#addFilterTag').val('');
        event.preventDefault();
    });
    
    // Stream text filter submit
    $('#addFilterTextSubmit').click(function(event){
        $('#stream').empty();
        $(document).trigger('refreshStream', {id: getFirstStreamId(), tags: tagsFilter, text: $('#addFilterText').val()});
        event.preventDefault();
    });
    
    $('.groupStream').attr('url', url('data/stream/group/' + model.groupId));
    $('.myStream').attr('url', url('data/stream/mystream'));
    $('.globalStream').attr('url', url('data/stream/global'));

    // Load streams on load
    if($.exists($('#stream'))) {
        $(document).trigger('refreshStream', {id: getFirstStreamId()});
    }
    
	// Set forms focus
	$('#linkForm :input:visible:enabled:first').focus();

	// Link comments to thread page
	$('.TypeLinkThread').live('click', showThread);
	
	// Link profiles to profile page
	$('.TypeLinkProfile').live('click', showProfile);
	
	// Link groups to group page
	$('.TypeLinkGroup').live('click', showGroup);
	
});
