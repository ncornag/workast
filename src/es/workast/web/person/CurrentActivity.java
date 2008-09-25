package es.workast.web.person;

import java.io.Serializable;

import org.joda.time.DateTime;

import es.workast.model.activity.Activity;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class CurrentActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------- Constructors

    public CurrentActivity() {

    }

    public CurrentActivity(Activity activity) {
        this.id = activity.getId();
        this.postedTime = activity.getPostedTime();
        this.type = activity.getType();
    }

    // ---------- Properties

    private Long id;

    private DateTime postedTime = new DateTime();

    private String type;

    private String message;

    private String renderedMessage;

    // ---------- Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(DateTime postedTime) {
        this.postedTime = postedTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRenderedMessage() {
        return renderedMessage;
    }

    public void setRenderedMessage(String renderedMessage) {
        this.renderedMessage = renderedMessage;
    }

}
