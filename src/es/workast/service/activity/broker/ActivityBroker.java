package es.workast.service.activity.broker;

import es.workast.model.activity.Activity;

/**
 * @author Nicol�s Cornaglia
 */
public interface ActivityBroker {

    /**
     * Publish the activity
     * 
     * @param activity
     */
    void publish(Activity activity);

}
