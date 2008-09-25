package es.workast.model.activity.type.event;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import es.workast.model.activity.ActivityData;
import es.workast.utils.ReadableDateTimeAdapter;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.NONE)
public class EventActivityData implements ActivityData, Serializable {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String type = EventActivityType.TYPE;

    @XmlElement
    @XmlJavaTypeAdapter(ReadableDateTimeAdapter.class)
    private DateTime startDate;

    @XmlElement
    @XmlJavaTypeAdapter(ReadableDateTimeAdapter.class)
    private DateTime endDate;

    // ---------- Constructors

    public EventActivityData() {
    }

    public EventActivityData(DateTime startDate, DateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // ---------- Accessors

    /**
     * @see es.workast.model.activity.ActivityData#getType()
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

}
