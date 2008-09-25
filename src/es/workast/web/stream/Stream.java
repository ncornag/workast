package es.workast.web.stream;

import java.util.Collection;

import es.workast.model.activity.Activity;
import es.workast.model.person.Person;

/**
 * @author Nicolás Cornaglia
 * 
 */
public class Stream {

    private Collection<Activity> activities;
    private Person person;
    private Long pollInterval;

    public Stream() {

    }

    public Stream(Person person, Collection<Activity> activities, Long pollInterval) {
        this.person = person;
        this.activities = activities;
        this.pollInterval = pollInterval;
    }

    public Person getPerson() {
        return person;
    }

    public Collection<Activity> getActivities() {
        return activities;
    }

    public Long getPollInterval() {
        return pollInterval;
    }

}
