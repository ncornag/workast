package es.workast.service.person.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.workast.core.security.SecurityUser;
import es.workast.dao.person.PendingPasswordDao;
import es.workast.dao.person.PersonDao;
import es.workast.model.activity.Activity;
import es.workast.model.activity.type.newuser.NewUserActivityTypeImpl;
import es.workast.model.group.Group;
import es.workast.model.person.PendingPassword;
import es.workast.model.person.Person;
import es.workast.service.activity.ActivityManager;
import es.workast.service.group.GroupManager;
import es.workast.service.group.impl.GroupNotFoundException;
import es.workast.service.person.PersonManager;
import es.workast.utils.PropertyUtils;
import es.workast.utils.SecurityUtils;
import es.workast.utils.mail.MailManager;
import es.workast.utils.mail.SendMailException;
import es.workast.web.person.PersonStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service("personManager")
public class PersonManagerImpl implements PersonManager {

    // ---------- Properties

    public static final String PROFILEPICTURESPATH = "profilePictures.path";
    public static final String SERVERPATH = "server.path";

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PersonDao personDao;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PendingPasswordDao pendingPasswordDao;
    @Autowired
    private MailManager mailManager;

    private String profilePicturesPath = null;
    private String serverPath = null;

    // ---------- Methods

    /**
     * @see es.workast.service.person.PersonManager#getPersonByUsername(java.lang.String)
     */
    public Person getPersonByUsername(String username) {
        return personDao.getPersonByUsername(username);
    }

    /**
     * @see es.workast.service.person.PersonManager#savePerson(es.workast.model.person.Person)
     */
    @Transactional(readOnly = false)
    public void savePerson(Person person) {
        personDao.saveOrUpdate(person);
    }

    /**
     * @see es.workast.service.person.PersonManager#setCurrentActivity(es.workast.model.activity.Activity)
     */
    public void setCurrentActivity(Activity activity) {
        activity.getPerson().setCurrentActivity(activity);
        savePerson(activity.getPerson());
    }

    /**
     * @see es.workast.service.person.PersonManager#getCurrentPerson()
     */
    public Person getCurrentPerson() {
        return personDao.get(((SecurityUser) SecurityUtils.getCurrentUser()).getId());
    }

    /**
     * @see es.workast.service.person.PersonManager#saveCurrentPerson()
     */
    @Transactional(readOnly = false)
    public Person createPerson(Person person) {

        String password = person.getPassword();
        person.setPassword("");
        savePerson(person);

        person.setPassword(passwordEncoder.encodePassword(password, person.getId()));
        savePerson(person);

        joinGroup(person, groupManager.getGroup(Group.GLOBAL_GROUP_ID));

        Activity newUserActivity = new Activity(person, groupManager.getGroup(Group.GLOBAL_GROUP_ID), NewUserActivityTypeImpl.TYPE);
        newUserActivity.setMessage("");
        newUserActivity.setRenderedMessage("");
        activityManager.saveActivity(newUserActivity, false);

        return person;
    }

    /**
     * @see es.workast.service.person.PersonManager#findPersons(java.lang.String)
     */
    public Collection<Person> findPersons(String query) {
        return personDao.findByName(query);
    }

    /**
     * @see es.workast.service.person.PersonManager#getPerson(java.lang.Long)
     */
    public Person getPerson(Long personId) {
        return personDao.get(personId);
    }

    /**
     * @see es.workast.service.person.PersonManager#getStatistics(es.workast.model.person.Person)
     */
    @SuppressWarnings("unchecked")
    public PersonStatistic getStatistics(Person person) {
        // Get Stats
        int daysBefore = 13;
        ReadableDateTime dateFrom = new DateMidnight().minusDays(daysBefore);
        List<Object[]> results = personDao.getStatistics(person, dateFrom);
        // Build map
        Map<Date, Long> stats = new HashMap<Date, Long>();
        for (Object[] result : results) {
            stats.put((Date) result[0], ((Number) result[1]).longValue());
        }
        // Fill the array
        List<Long> activityCount = new ArrayList<Long>();
        DateTime initDate = new DateTime(dateFrom);
        for (int i = daysBefore; i >= 0; i--) {
            Long dayStat = stats.get(initDate.toDate());
            activityCount.add(dayStat == null ? 0 : dayStat);
            initDate = initDate.plusDays(1);
        }
        // Make object
        PersonStatistic personStatistic = new PersonStatistic(person, dateFrom, activityCount);
        // return!
        return personStatistic;
    }

    /**
     * @see es.workast.service.person.PersonManager#joinGroup(es.workast.model.person.Person, es.workast.model.group.Group)
     */
    @Transactional(readOnly = false)
    public void joinGroup(Person person, Group group) {
        if (person == null) {
            throw new PersonNotFoundException();
        }
        if (group == null) {
            throw new GroupNotFoundException();
        }
        group.getMembers().add(person);
        groupManager.saveGroup(group);
        personDao.flushAndRefresh(person);
    }

    /**
     * @see es.workast.service.person.PersonManager#leaveGroup(es.workast.model.person.Person, es.workast.model.group.Group)
     */
    @Transactional(readOnly = false)
    public void leaveGroup(Person person, Group group) {
        if (person == null) {
            throw new PersonNotFoundException();
        }
        if (group == null) {
            throw new GroupNotFoundException();
        }
        group.getMembers().remove(person);
        groupManager.saveGroup(group);
        personDao.flushAndRefresh(person);
    }

    /**
     * @see es.workast.service.person.PersonManager#follow(es.workast.model.person.Person, es.workast.model.person.Person)
     */
    @Transactional(readOnly = false)
    public void follow(Person follower, Person followed) {
        if (follower == null) {
            throw new PersonNotFoundException();
        }
        if (followed == null) {
            throw new PersonNotFoundException();
        }
        followed.getFollowers().add(follower);
        savePerson(followed);
        personDao.flushAndRefresh(follower);
    }

    /**
     * @see es.workast.service.person.PersonManager#unfollow(es.workast.model.person.Person, es.workast.model.person.Person)
     */
    @Transactional(readOnly = false)
    public void unfollow(Person follower, Person followed) {
        if (follower == null) {
            throw new PersonNotFoundException();
        }
        if (followed == null) {
            throw new PersonNotFoundException();
        }
        followed.getFollowers().remove(follower);
        savePerson(followed);
        personDao.flushAndRefresh(follower);
    }

    /**
     * @see es.workast.service.person.PersonManager#regeneratePassword(es.workast.model.person.Person)
     */
    @Transactional(readOnly = false)
    public void regeneratePassword(Person person) {
        if (person == null) {
            throw new PersonNotFoundException();
        }

        String uuid = UUID.randomUUID().toString();

        PendingPassword pending = new PendingPassword();
        pending.setPerson(person);
        pending.setUid(uuid);
        pending.setInsertedDate(Calendar.getInstance().getTime());

        pendingPasswordDao.saveOrUpdate(pending);

        if (logger.isDebugEnabled()) {
            logger.debug(" El path para modificar el password es: /person/password/recoverPassword/" + uuid);
        }
        try {
            sendForgotPasswordMail(person, uuid);
        } catch (MessagingException e) {
            logger.error(e.getLocalizedMessage());
            throw new SendMailException(e); //TODO: I18n esto!
        }
    }

    @Transactional(readOnly = false)
    public void saveRecoveredPassword(Person person, String password) {
        person.setPassword(passwordEncoder.encodePassword(password, person.getId()));
        savePerson(person);
        pendingPasswordDao.deleteAllPendingPassword(person.getId());
    }

    /**
     * @param uid
     * @return
     */
    public Person getPersonByPendingUID(String uid) {
        return pendingPasswordDao.getPersonByPendingUID(uid);
    }

    // ---------- Accessors

    public String getServerPath() {
        if (serverPath == null) {
            serverPath = PropertyUtils.getProperty(SERVERPATH);
            if (serverPath.lastIndexOf("/") == serverPath.length() - 1) {
                serverPath = serverPath.substring(0, serverPath.length() - 1);
            }
        }
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getProfilePicturesPath() {
        if (profilePicturesPath == null) {
            profilePicturesPath = PropertyUtils.getProperty(PROFILEPICTURESPATH);
        }
        return profilePicturesPath;
    }

    public void setProfilePicturesPath(String profilePicturesPath) {
        this.profilePicturesPath = profilePicturesPath;
    }

    // --------- Helpers

    private void sendForgotPasswordMail(final Person person, final String uid) throws MessagingException {
        MimeMessageHelper helper = mailManager.createMimeMessageHelper();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("person", person);
        model.put("uid", uid);
        model.put("serverPath", getServerPath());
        helper.setText(mailManager.mergeTemplateIntoString("es/workast/person/forgot-password.vm", model), true);
        helper.setFrom("admin@workast.com");
        helper.setTo(person.getEmail());
        helper.setSubject("Password Regeneration"); //TO I18N
        mailManager.send(helper);
    }

}
