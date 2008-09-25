package es.workast.service.group.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.workast.dao.group.GroupDao;
import es.workast.model.activity.Activity;
import es.workast.model.activity.type.newgroup.NewGroupActivityData;
import es.workast.model.activity.type.newgroup.NewGroupActivityTypeImpl;
import es.workast.model.group.Group;
import es.workast.model.person.Person;
import es.workast.service.activity.ActivityManager;
import es.workast.service.group.GroupManager;
import es.workast.service.person.PersonManager;
import es.workast.web.group.GroupStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service("groupManager")
public class GroupManagerImpl implements GroupManager {

    // ---------- Services

    @Autowired
    private PersonManager personManager;
    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private GroupDao groupDao;

    // ---------- Methods

    /**
     * @see es.workast.service.group.GroupManager#saveGroup(es.workast.model.group.Group)
     */
    @Transactional(readOnly = false)
    public void saveGroup(Group group) {
        groupDao.saveOrUpdate(group);
    }

    /**
     * @see es.workast.service.group.GroupManager#getGroup(java.lang.Long)
     */
    public Group getGroup(Long id) {
        return groupDao.get(id);
    }

    /**
     * @see es.workast.service.group.GroupManager#getGroupsByName(java.lang.String)
     */
    public List<GroupStatistic> getGroupsByName(String name) {
        return groupDao.getGroupStatistics(name, personManager.getCurrentPerson(), false);
    }

    /**
     * @see es.workast.service.group.GroupManager#createGroup(es.workast.model.group.Group)
     */
    @Transactional(readOnly = false)
    public Group createGroup(Group group) {

        Person currentPerson = personManager.getCurrentPerson();
        group.setAdmin(currentPerson);

        saveGroup(group);

        Activity newGroupActivity = new Activity(currentPerson, getGroup(Group.GLOBAL_GROUP_ID), NewGroupActivityTypeImpl.TYPE);
        newGroupActivity.setMessage("");
        newGroupActivity.setRenderedMessage("");
        newGroupActivity.setActivityData(new NewGroupActivityData(group.getId(), group.getName()));
        activityManager.saveActivity(newGroupActivity, false);

        personManager.joinGroup(currentPerson, group);

        return group;
    }

    // ---------- Accessors

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public PersonManager getPersonManager() {
        return personManager;
    }

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

}
