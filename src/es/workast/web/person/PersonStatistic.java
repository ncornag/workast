package es.workast.web.person;

import java.util.List;

import org.joda.time.ReadableDateTime;

import es.workast.model.person.Person;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class PersonStatistic {

    private Person person;

    private ReadableDateTime dateFrom;

    private List<Long> activityCount;

    public PersonStatistic() {

    }

    public PersonStatistic(Person person, ReadableDateTime dateFrom, List<Long> activityCount) {
        this.person = person;
        this.dateFrom = dateFrom;
        this.activityCount = activityCount;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ReadableDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(ReadableDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public List<Long> getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(List<Long> activityCount) {
        this.activityCount = activityCount;
    }

}
