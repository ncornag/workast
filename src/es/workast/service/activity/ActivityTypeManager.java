package es.workast.service.activity;

import es.workast.model.activity.type.ActivityType;

/**
 * @author Nicol�s Cornaglia
 */
public interface ActivityTypeManager {

    /**
     * @param type
     * @return
     */
    ActivityType getActivityType(String type);

}
