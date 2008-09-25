package es.workast.service.activity.broker.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.workast.model.activity.Activity;
import es.workast.service.activity.broker.ActivityBroker;
import es.workast.service.activity.broker.ActivitySubscriber;
import es.workast.service.activity.cache.ActivityCache;

/**
 * Broadcast activities to subscribers (Classes implementing {@link ActivitySubscriber})
 * <p>
 * i.e.: {@link ActivityCache} and Broadcaster
 * 
 * @author Nicolás Cornaglia
 */
@Service("activityBroker")
public class ActivityBrokerImpl implements ActivityBroker {

    @Autowired
    private ActivitySubscriber[] activitySubscribers;

    public void publish(Activity activity) {
        for (ActivitySubscriber subscriber : activitySubscribers) {
            subscriber.acceptActivity(activity);
        }

    }

}
