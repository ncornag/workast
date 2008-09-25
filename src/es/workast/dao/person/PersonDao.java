package es.workast.dao.person;

import java.util.Collection;
import java.util.List;

import org.joda.time.ReadableDateTime;

import es.workast.core.persistence.AbstractDao;
import es.workast.model.person.Person;

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
    Person getPersonByUsername(String username);

    /**
     * @param query
     * @return
     */
    Collection<Person> findByName(String query);

    /**
     * @param person
     * @param fromDate
     * @return
     */
    @SuppressWarnings("unchecked")
    List getStatistics(Person person, ReadableDateTime fromDate);

    /**
     * @param object
     */
    void flushAndRefresh(Person object);

}
