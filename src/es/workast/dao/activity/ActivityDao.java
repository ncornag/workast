package es.workast.dao.activity;

import java.util.List;

import es.workast.core.persistence.AbstractDao;
import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.web.stream.StreamFilter;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface ActivityDao extends AbstractDao<Activity, Long> {

    /**
     * @param streamFilter
     * @return
     */
    List<Activity> getGlobalActivities(Person person, StreamFilter streamFilter);

    /**
     * @param groupId
     * @param streamFilter
     * @return
     */
    List<Activity> getGroupActivities(Person person, Long groupId, StreamFilter streamFilter);

    /**
     * @param person
     * @param streamFilter
     * @return
     */
    List<Activity> getPersonStreamActivities(Person person, StreamFilter streamFilter);

    /**
     * @param person    The owner of the activities
     * @param tagsPerson    The owner of the tags
     * @param streamFilter
     * @return
     */
    List<Activity> getProfileStreamActivities(Person person, Person tagsPerson, StreamFilter streamFilter);

}
