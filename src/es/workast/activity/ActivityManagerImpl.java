package es.workast.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.workast.activity.persistence.ActivityDao;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service("activityManager")
public class ActivityManagerImpl implements ActivityManager {

    // ---------- Properties

    private Map<String, ActivityType> activityTypes = new HashMap<String, ActivityType>();

    // ---------- Services

    private ActivityDao activityDao;

    // ---------- Methods

    /**
     * @see es.workast.activity.ActivityManager#getActivities(java.lang.Long, java.lang.Long, int, int)
     */
    public List<Activity> getActivities(Long personId, Long groupId, int min, int max) {
        return activityDao.getActivities(personId, groupId, min, max);
    }

    /**
     * @see es.workast.activity.ActivityManager#getActivityType(java.lang.String)
     */
    public ActivityType getActivityType(String type) {
        return activityTypes.get(type);
    }

    // ---------- Accessors
    @Autowired
    public void setActivityTypes(ActivityType[] activityTypes) {
        for (ActivityType activityType : activityTypes) {
            this.activityTypes.put(activityType.getType(), activityType);
        }
    }

    public ActivityDao getActivityDao() {
        return activityDao;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

}
