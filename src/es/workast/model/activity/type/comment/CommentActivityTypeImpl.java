package es.workast.model.activity.type.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.workast.core.render.RenderEngine;
import es.workast.model.activity.Activity;
import es.workast.model.activity.ActivityData;
import es.workast.model.activity.type.ActivityType;
import es.workast.model.person.Person;
import es.workast.service.activity.ActivityManager;
import es.workast.web.activity.CommentActivityFormDTO;

/**
 * @author Nicolás Cornaglia
 */
@Component
public class CommentActivityTypeImpl implements CommentActivityType, ActivityType {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    // ---------- Services

    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private RenderEngine renderEngine;

    // ---------- Methods

    /**
     * @see es.workast.model.activity.type.ActivityType#getType()
     */
    public String getType() {
        return TYPE;
    }

    /**
     * @see es.workast.model.activity.type.ActivityType#getActivity(es.workast.model.person.Person, java.lang.Object)
     */
    public Activity getActivity(Person person, Object objectDTO) {

        CommentActivityFormDTO dto = (CommentActivityFormDTO) objectDTO;

        Activity parentActivity = activityManager.getActivity(dto.getActivityId());
        Activity activity = new Activity(person, parentActivity.getGroup(), CommentActivityType.TYPE);

        activity.setMessage(dto.getComment());
        activity.setRenderedMessage(renderEngine.wikiRender(dto.getComment()));

        parentActivity.addRelated(activity);

        return activity;
    }

    /**
     * @see es.workast.model.activity.type.ActivityType#getData(java.lang.String)
     */
    public ActivityData getData(String data) {
        return null;
    }

    /**
     * @see es.workast.model.activity.type.ActivityType#getData(es.workast.model.activity.ActivityData)
     */
    public String getData(ActivityData data) {
        return null;
    }

}
