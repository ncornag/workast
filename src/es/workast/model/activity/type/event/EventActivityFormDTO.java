package es.workast.model.activity.type.event;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Nicolás Cornaglia
 */
public class EventActivityFormDTO {

    // ---------- Properties

    private Long groupId;

    @Size(min = 1, max = 50)
    private String title;

    @NotEmpty
    private Date startDate;

    private Date endDate;

    // ---------- Accessors

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
