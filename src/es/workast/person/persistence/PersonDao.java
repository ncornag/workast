package es.workast.person.persistence;

import es.workast.core.persistence.AbstractDao;
import es.workast.person.Person;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface PersonDao extends AbstractDao<Person, Long> {

    /**
     * @param username
     * @return
     */
    Person getByUsername(String username);

}
