package es.workast.model.activity.type.newuser;

import org.springframework.stereotype.Component;

import es.workast.model.activity.Activity;
import es.workast.model.activity.ActivityData;
import es.workast.model.activity.type.ActivityType;
import es.workast.model.person.Person;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Component
public class NewUserActivityTypeImpl implements NewUserActivityType, ActivityType {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    // ---------- Services

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
    public Activity getActivity(Person person, Object dto) {
        // TODO Auto-generated method stub
        return null;
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
