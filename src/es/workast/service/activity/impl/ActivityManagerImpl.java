package es.workast.service.activity.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.workast.dao.activity.ActivityDao;
import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.model.tag.Tag;
import es.workast.service.activity.ActivityManager;
import es.workast.service.activity.broker.ActivityBroker;
import es.workast.service.activity.cache.ActivityCache;
import es.workast.service.activity.cache.ActivityCacheImpl;
import es.workast.service.person.PersonManager;
import es.workast.service.tag.TagManager;
import es.workast.web.stream.StreamFilter;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Component("activityManager")
public class ActivityManagerImpl implements ActivityManager {

    // ---------- Services

    @Autowired
    private ActivityCache activityCache;
    @Autowired
    private PersonManager personManager;
    @Autowired
    private TagManager tagManager;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityBroker activityBroker;

    // ---------- Methods

    /**
     * @see es.workast.service.activity.ActivityManager#getGlobalActivities(es.workast.model.person.Person, es.workast.web.stream.StreamFilter)
     */
    public List<Activity> getGlobalActivities(Person person, StreamFilter streamFilter) {
        return getOrUpdateFromCache(ActivityCache.GLOBAL_CACHE, 0L, person, streamFilter, null, null, new QueryCallback() {

            public List<Activity> doQuery(Person person, StreamFilter streamFilter, Person tagsPerson, Long groupId) {
                return activityDao.getGlobalActivities(person, streamFilter);
            }
        });
    }

    /**
     * @see es.workast.service.activity.ActivityManager#getGroupActivities(es.workast.model.person.Person, java.lang.Long, es.workast.web.stream.StreamFilter)
     */
    public List<Activity> getGroupActivities(Person person, Long groupId, StreamFilter streamFilter) {
        return getOrUpdateFromCache(ActivityCache.GROUP_CACHE, groupId, person, streamFilter, null, groupId, new QueryCallback() {

            public List<Activity> doQuery(Person person, StreamFilter streamFilter, Person tagsPerson, Long groupId) {
                return activityDao.getGroupActivities(person, groupId, streamFilter);
            }
        });
    }

    /**
     * @see es.workast.service.activity.ActivityManager#getPersonStreamActivities(es.workast.model.person.Person, es.workast.web.stream.StreamFilter)
     */
    public List<Activity> getPersonStreamActivities(Person person, StreamFilter streamFilter) {
        return getOrUpdateFromCache(ActivityCache.STREAM_CACHE, person.getId(), person, streamFilter, null, null, new QueryCallback() {

            public List<Activity> doQuery(Person person, StreamFilter streamFilter, Person tagsPerson, Long groupId) {
                return activityDao.getPersonStreamActivities(person, streamFilter);
            }
        });
    }

    /**
     * @see es.workast.service.activity.ActivityManager#getProfileStreamActivities(es.workast.model.person.Person, es.workast.model.person.Person, es.workast.web.stream.StreamFilter)
     */
    public List<Activity> getProfileStreamActivities(Person person, Person tagsPerson, StreamFilter streamFilter) {
        return getOrUpdateFromCache(ActivityCache.PROFILE_CACHE, person.getId(), person, streamFilter, tagsPerson, null, new QueryCallback() {

            public List<Activity> doQuery(Person person, StreamFilter streamFilter, Person tagsPerson, Long groupId) {
                return activityDao.getProfileStreamActivities(person, tagsPerson, streamFilter);
            }
        });
    }

    /**
     * @param streamFilter
     * @param callBack
     * @return
     */
    private List<Activity> getOrUpdateFromCache(Long cacheId, Long listId, Person person, StreamFilter streamFilter, Person tagsPerson, Long groupId, QueryCallback callBack) {
        if (isCacheable(streamFilter)) {
            List<Activity> activities = activityCache.getCachedList(cacheId, listId);
            if (activities == null) {
                activities = activityCache.setCachedList(cacheId, listId, callBack.doQuery(person, streamFilter, tagsPerson, groupId));
            }
            return getFilteredList(activities, streamFilter.getFirstId());
        } else {
            return callBack.doQuery(person, streamFilter, tagsPerson, groupId);
        }
    }

    /**
     * @param streamFilter
     * @return
     */
    private boolean isCacheable(StreamFilter streamFilter) {
        return !streamFilter.getBackwards() && streamFilter.getTags() == null && "".equals(streamFilter.getText()) && "".equals(streamFilter.getType());
    }

    /**
     * Filter the activities.id > firstId
     * 
     * @param activities
     * @param firstId
     * @return
     */
    private List<Activity> getFilteredList(List<Activity> activities, Long firstId) {
        List<Activity> result = new ArrayList<Activity>();
        synchronized (activities) {
            for (Activity activity : activities) {
                if (activity.getId() > firstId) {
                    result.add(activity);
                } else {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @see es.workast.service.activity.ActivityManager#getActivity(java.lang.Long)
     */
    public Activity getActivity(Long activityId) {
        return activityDao.get(activityId);
    }

    /**
     * @see es.workast.service.activity.ActivityManager#saveActivity(es.workast.model.activity.Activity, boolean)
     */
    @Transactional(readOnly = false)
    public void saveActivity(Activity activity, boolean isCurrent) {
        activityDao.saveOrUpdate(activity);
        Activity parentActivity = activity.getParentActivity();
        if (parentActivity != null && parentActivity.getParentActivity() != null) {
            activityDao.saveOrUpdate(parentActivity.getParentActivity());
            ((ActivityCacheImpl) activityCache).refreshActivity(parentActivity.getParentActivity());
        } else if (parentActivity != null) {
            activityDao.saveOrUpdate(parentActivity);
            ((ActivityCacheImpl) activityCache).refreshActivity(parentActivity);
        }
        if (isCurrent) {
            getPersonManager().setCurrentActivity(activity);
        }
        activityBroker.publish(activity);
    }

    /**
     * @see es.workast.service.activity.ActivityManager#clearStatus()
     */
    @Transactional(readOnly = false)
    public void clearStatus() {
        Person person = getPersonManager().getCurrentPerson();
        if (person.getCurrentActivity() != null) {
            person.setCurrentActivity(null);
            getPersonManager().savePerson(person);
        }
    }

    /**
     * @see es.workast.service.activity.ActivityManager#addActivityTag(es.workast.model.activity.Activity, java.lang.String)
     */
    @Transactional(readOnly = false)
    public void addActivityTag(Activity activity, String tagName) {
        Tag tag = tagManager.getTagByName(tagName);
        if (tag == null) {
            tag = new Tag(tagName);
            tagManager.saveTag(tag);
        }

        if (!activity.hasTag(tag)) {
            activity.addTag(personManager.getCurrentPerson(), tag);
            saveActivity(activity, false);
        }
    }

    /**
     * @see es.workast.service.activity.ActivityManager#removeActivityTag(es.workast.model.activity.Activity, es.workast.model.tag.Tag)
     */
    @Transactional(readOnly = false)
    public void removeActivityTag(Activity activity, Tag tag) {
        if (activity.hasTag(tag)) {
            activity.removeTag(personManager.getCurrentPerson(), tag);
            saveActivity(activity, false);
        }
    }

    // ---------- Accessors

    public ActivityDao getActivityDao() {
        return activityDao;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public PersonManager getPersonManager() {
        return personManager;
    }

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public void setTagManager(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    public ActivityCache getActivityCacheManager() {
        return activityCache;
    }

    public void setActivityCacheManager(ActivityCache activityCache) {
        this.activityCache = activityCache;
    }

    public ActivityBroker getActivityBroker() {
        return activityBroker;
    }

    public void setActivityBroker(ActivityBroker activityBroker) {
        this.activityBroker = activityBroker;
    }

}
