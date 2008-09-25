package es.workast.model.activity.type;

import es.workast.model.activity.Activity;
import es.workast.model.activity.ActivityData;
import es.workast.model.person.Person;

/**
 * @author Nicolás Cornaglia
 */
public interface ActivityType {

    /**
     * @param type
     * @return
     */
    String getType();

    /**
     * @param person
     * @param dto
     * @return
     */
    Activity getActivity(Person person, Object dto);

    /**
     * @param data JSON String object
     * @return  the entity
     */
    ActivityData getData(String data);

    /**
     * @param data the entity
     * @return JSON String object
     */
    String getData(ActivityData data);

}
