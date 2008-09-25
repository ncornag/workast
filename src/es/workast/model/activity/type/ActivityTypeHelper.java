package es.workast.model.activity.type;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import es.workast.service.activity.ActivityTypeManager;

/**
 * @author Nicolás Cornaglia
 */
@Component
public final class ActivityTypeHelper {

    private static ActivityTypeManager activityTypeManager;

    public static ActivityType getActivityType(String type) {
        return activityTypeManager.getActivityType(type);
    }

    @Resource
    public void setActivityTypeManager(ActivityTypeManager activityTypeManager) {
        ActivityTypeHelper.activityTypeManager = activityTypeManager;
    }
}
