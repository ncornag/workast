package es.workast.activity;

import java.util.List;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface ActivityManager {

    /**
     * @param personId
     * @param groupId
     * @param min
     * @param max
     * @return
     */
    public abstract List<Activity> getActivities(Long personId, Long groupId, int min, int max);

    /**
     * @param type
     * @return
     */
    public abstract ActivityType getActivityType(String type);

}