package es.workast.stream;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.workast.activity.Activity;

/**
 * @author Nicolás Cornaglia
 * 
 */
@XmlRootElement(name = "stream")
public class Stream {

    private Collection<Activity> activities;

    @XmlElement(name = "activities")
    public Collection<Activity> getStream() {
        return activities;
    }

    public void setStream(Collection<Activity> activities) {
        this.activities = activities;
    }

}
