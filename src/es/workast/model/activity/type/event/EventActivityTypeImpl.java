package es.workast.model.activity.type.event;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import es.workast.core.json.JsonManager;
import es.workast.model.activity.Activity;
import es.workast.model.activity.ActivityData;
import es.workast.model.activity.type.ActivityType;
import es.workast.model.person.Person;
import es.workast.service.group.GroupManager;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Component
public class EventActivityTypeImpl implements EventActivityType, ActivityType {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    // ---------- Services

    @Autowired
    private GroupManager groupManager;
    @Autowired
    @Qualifier("active")
    private JsonManager jsonManager;

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
        EventActivityFormDTO dto = (EventActivityFormDTO) objectDTO;

        Activity activity = new Activity(person, groupManager.getGroup(dto.getGroupId()), EventActivityType.TYPE);

        activity.setMessage(dto.getTitle());
        activity.setRenderedMessage(dto.getTitle());
        //activity.setRenderedMessage(renderEngine.wikiRender(message));

        DateTime startDate = new DateTime(dto.getStartDate()).toDateTime(DateTimeZone.forID("UTC"));
        DateTime endDate = dto.getEndDate() == null ? startDate.plusDays(1) : new DateTime(dto.getEndDate()).toDateTime(DateTimeZone.forID("UTC")).plusDays(1);

        activity.setActivityData(new EventActivityData(startDate, endDate));

        return activity;
    }

    /**
     * @see es.workast.model.activity.type.ActivityType#getData(java.lang.String)
     */
    public ActivityData getData(String data) {
        return jsonManager.unMarshall(data, EventActivityData.class);
    };

    /**
     * @see es.workast.model.activity.type.ActivityType#getData(es.workast.model.activity.ActivityData)
     */
    public String getData(ActivityData data) {
        return jsonManager.marshall(data);
    }

}
