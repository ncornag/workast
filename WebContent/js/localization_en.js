var activityMessageTemplates = {
        STATUS: "{renderedMessage}", 
        COMMENT: "<a class='TypeLinkThread' id='commentedTo_{parentId}' href='#' onMouseOver=\"Tip('{replyToRenderedMessage}', BALLOON, true, ABOVE, true, OFFSETX, -30)\" onmouseout='UnTip()'>commented to</a> <a class='profile TypeLinkProfile' id='commenterProfile_{replyToOwnerId}' href='#'>{replyToOwnerName}:</a> {renderedMessage}",
        EVENT: "{renderedMessage} (from {startDateFormatted} to {endDateFormatted})", 
        NEWGROUP: "has created the <a class='TypeLinkGroup' id='group_{data_groupId}' href='#'>{data_groupName}</a> group.",
        NEWUSER: "has joined the network."
}