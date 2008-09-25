package es.workast.service.activity.cache;

import es.workast.model.activity.Activity;
import es.workast.model.person.Person;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class GroupsStrategy implements ActivityAcceptStrategy {

    /**
     * @see es.workast.service.activity.cache.ActivityAcceptStrategy#accept(es.workast.model.person.Person, es.workast.model.activity.Activity)
     */
    public boolean accept(Person person, Activity activity) {
        return person.getGroups().contains(activity.getGroup());
    }

}
