package es.workast.service.person;

import java.util.Collection;

import es.workast.model.activity.Activity;
import es.workast.model.group.Group;
import es.workast.model.person.Person;
import es.workast.service.group.impl.GroupNotFoundException;
import es.workast.service.person.impl.PersonNotFoundException;
import es.workast.utils.mail.SendMailException;
import es.workast.web.person.PersonStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface PersonManager {

    // ---------- Properties

    String PERSONNOTFOUND = "Person not found.";
    String PASSWORD_MISMATCH = "Password mismatch.";
    String PASSWORD_LENGHT = "Password legth.";
    String EMAIL_ALREADY_USED = "Email already in use.";
    String CURRENT_PASSWORD_MISMATCH = "Current password mismatch.";
    String EMPTY_FIELD = "Empty mandatory field.";
    String BIND_ERROR = "Cannot bind properties.";

    /**
     * Returns a {@link Person} by his username
     * 
     * @param username
     * @return {@link Person}
     */
    Person getPersonByUsername(String username);

    /**
     * Persists a {@link Person} to the database
     * 
     * @param person
     */
    void savePerson(Person person);

    /**
     * @param person
     * @param activity
     */
    void setCurrentActivity(Activity activity);

    /**
     * @param query
     * @return
     */
    Collection<Person> findPersons(String query);

    /**
     * Joins a {@link Person} to a {@link Group}
     * 
     * @param person
     * @param group
     * @throws PersonNotFoundException
     * @throws GroupNotFoundException
     */
    void joinGroup(Person person, Group group) throws PersonNotFoundException, GroupNotFoundException;

    /**
     * Unjoins the current {@link Person} from a {@link Group}
     * 
     * @param person
     * @param group
     * @throws PersonNotFoundException
     * @throws GroupNotFoundException
     */
    void leaveGroup(Person person, Group group) throws PersonNotFoundException, GroupNotFoundException;

    /**
     * Returns the currently logged {@link Person}
     * 
     * @return {@link Person}
     */
    Person getCurrentPerson();

    /**
     * Creates a new {@link Person}
     * 
     * @return
     */
    Person createPerson(Person person);

    /**
    * Returns a {@link Person} by ID
    * 
    * @param personId
    * @return {@link Person}
    */
    Person getPerson(Long personId);

    /**
     * Returns the {@link Person} activity statistic 
     * 
     * @param personId
     * @return {@link PersonStatistic}
     */
    PersonStatistic getStatistics(Person person);

    /**
     * Connects two persons
     * 
     * @param follower
     * @param followed
     * @throws PersonNotFoundException
     */
    void follow(Person follower, Person followed) throws PersonNotFoundException;

    /**
     * Disconnects two persons
     * 
     * @param follower
     * @param followed
     * @throws PersonNotFoundException
     */
    void unfollow(Person follower, Person followed) throws PersonNotFoundException;

    /**
     * @param person
     */
    void regeneratePassword(Person person) throws PersonNotFoundException, SendMailException;

    /**
     * @param person
     * @param password
     * @return
     */
    void saveRecoveredPassword(Person person, String password);

    /**
     * @param uid
     * @return
     */
    Person getPersonByPendingUID(String uid);

    /**
     * @return
     */
    public String getProfilePicturesPath();

}
