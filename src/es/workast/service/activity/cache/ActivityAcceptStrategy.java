package es.workast.service.activity.cache;

import es.workast.model.activity.Activity;
import es.workast.model.person.Person;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface ActivityAcceptStrategy {

    boolean accept(Person person, Activity activity);

}
