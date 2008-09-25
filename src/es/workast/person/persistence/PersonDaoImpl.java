package es.workast.person.persistence;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import es.workast.core.persistence.AbstractHibernateDao;
import es.workast.person.Person;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Repository("personDao")
public class PersonDaoImpl extends AbstractHibernateDao<Person, Long> implements PersonDao {

    /**
     * @see es.workast.person.persistence.PersonDao#getByUsername(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Person getByUsername(String username) throws DataAccessException {
        List<Person> users = getHibernateTemplate().find("from Person p where p.username = '" + username + "'");
        if (users.size() == 0) {
            return null;
        } else if (users.size() > 1) {
            throw new DataIntegrityViolationException("Duplicate user: " + username);
        } else {
            return users.get(0);
        }
    }

}
