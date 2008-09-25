package es.workast.web.activity;

/**
 * @author Nicolás Cornaglia
 */
public class CommentActivityFormDTO {

    // ---------- Properties

    private Long activityId;

    private String comment;

    // ---------- Accessors

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
