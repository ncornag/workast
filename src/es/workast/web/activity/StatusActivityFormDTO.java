package es.workast.web.activity;

import java.util.List;

/**
 * @author Nicolás Cornaglia
 */
public class StatusActivityFormDTO {

    // ---------- Properties

    private Long groupId;

    private String message;

    private String current;

    private List<String> attachmentIds;

    // ---------- Accessors

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public List<String> getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(List<String> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

}
