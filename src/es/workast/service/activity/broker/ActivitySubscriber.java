package es.workast.service.activity.broker;

import es.workast.model.activity.Activity;

/**
 * @author Nicolás Cornaglia
 */
public interface ActivitySubscriber {

    /**
     * @param activity
     */
    void acceptActivity(Activity activity);

}
