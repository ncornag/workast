package es.workast.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.workast.person.persistence.PersonDao;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service("personManager")
public class PersonManagerImpl implements PersonManager {

    // ---------- Services

    @Autowired
    PersonDao personDao;

    // ---------- Methods

    /**
     * @see es.workast.person.PersonManager#getByUsername(java.lang.String)
     */
    @Override
    public Person getByUsername(String username) {
        return personDao.getByUsername(username);
    }

    // ---------- Accessors

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

}
