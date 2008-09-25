package es.workast.service.activity;

import java.util.List;

import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.model.tag.Tag;
import es.workast.web.stream.StreamFilter;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface ActivityManager {

    /**
     * @param activityId
     * @return
     */
    Activity getActivity(Long activityId);

    /**
     * @param activity
     * @param isCurrent
     */
    void saveActivity(Activity activity, boolean isCurrent);

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
     * @param person
     * @param streamFilter
     * @return
     */
    List<Activity> getProfileStreamActivities(Person person, Person tagsPerson, StreamFilter streamFilter);

    /**
     * 
     */
    void clearStatus();

    /**
     * @param activity
     * @param tag
     */
    public void addActivityTag(Activity activity, String tagName);

    /**
     * @param activity
     * @param tag
     */
    public void removeActivityTag(Activity activity, Tag tag);

}