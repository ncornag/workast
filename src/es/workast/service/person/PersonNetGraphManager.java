package es.workast.service.person;

import es.workast.model.person.Person;
import es.workast.web.person.net.Net;

/**
 * TODO Documentar
 * 
 * @author Nicol�s Cornaglia
 */
public interface PersonNetGraphManager {

    /**
     * @param person
     * @return
     */
    Net getPersonNet(Person person);

}