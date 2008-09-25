package es.workast.service.activity.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.workast.model.activity.type.ActivityType;
import es.workast.service.activity.ActivityTypeManager;

/**
 * @author Nicolás Cornaglia
 */
@Service("activityTypeManager")
public class ActivityTypeManagerImpl implements ActivityTypeManager {

    // ---------- Properties

    @SuppressWarnings("unused")
    @Autowired
    private ActivityType activityTypes[];

    private Map<String, ActivityType> activityTypesMap = new HashMap<String, ActivityType>();

    // ---------- Services

    @PostConstruct
    public void init() {
        for (int i = 0; i < activityTypes.length; i++) {
            activityTypesMap.put((activityTypes[i]).getType(), activityTypes[i]);
        }
    }

    /**
     * @see es.workast.service.activity.ActivityTypeManager#getActivityType(java.lang.String)
     */
    public ActivityType getActivityType(String type) {
        return activityTypesMap.get(type);
    }

    // ---------- Accessors

    /**
     * @param activityTypes the activityTypes to set
     */
    public void setActivityTypes(ActivityType[] activityTypes) {
        this.activityTypes = activityTypes;
    }

}
