package es.workast.service.activity.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.service.activity.broker.ActivitySubscriber;

/**
 * TODO Documentar
 * <p>
 * FIXME Clean old queues from 'userCache' in 'cleanCache' method
 * 
 * @author Nicolás Cornaglia
 */
@Service("activityCache")
//@Controller
public class ActivityCacheImpl implements ActivityCache, ActivitySubscriber {

    // ---------- Properties

    private Map<Long, Map<Long, LinkedList<Activity>>> caches;

    // ---------- Services

    // ---------- Methods

    @PostConstruct
    public void init() {
        cleanCache();
    }

    /**
     * @see es.workast.service.activity.cache.ActivityCache#addCache(java.lang.Long)
     */
    public void addCache(Long cacheId) {
        caches.put(cacheId, new HashMap<Long, LinkedList<Activity>>());
    }

    /**
     * @see es.workast.service.activity.cache.ActivityCache#getCachedList(java.lang.Long, java.lang.Long)
     */
    public List<Activity> getCachedList(Long cacheId, Long listId) {
        Map<Long, LinkedList<Activity>> cache = caches.get(cacheId);
        return cache.get(listId);
    }

    /**
     * @see es.workast.service.activity.cache.ActivityCache#setCachedList(java.lang.Long, java.lang.Long, java.util.List)
     */
    public List<Activity> setCachedList(Long cacheId, Long listId, List<Activity> data) {
        LinkedList<Activity> list = null;
        Map<Long, LinkedList<Activity>> cache = caches.get(cacheId);
        synchronized (cache) {
            list = new LinkedList<Activity>(data);
            cache.put(listId, list);
        }
        return list;
    }

    /**
     * @see es.workast.service.activity.cache.ActivityCache#refreshActivity(es.workast.model.activity.Activity)
     */
    public void refreshActivity(Activity activity) {

        // TODO: refactorizar

        // Refresh in global
        Map<Long, LinkedList<Activity>> globalCache = caches.get(GLOBAL_CACHE);
        LinkedList<Activity> globalList = globalCache.get(0L);
        if (globalList != null) {
            synchronized (globalList) {
                int index = globalList.indexOf(activity);
                if (index != -1) {
                    globalList.set(index, activity);
                }
            }
        }

        // Refresh in group activities
        Map<Long, LinkedList<Activity>> groupCache = caches.get(GROUP_CACHE);
        LinkedList<Activity> groupList = groupCache.get(activity.getGroup().getId());
        if (groupList != null) {
            synchronized (groupList) {
                int index = groupList.indexOf(activity);
                if (index != -1) {
                    groupList.set(index, activity);
                }
            }
        }

        // Refresh in follower streams
        Map<Long, LinkedList<Activity>> streamCache = caches.get(STREAM_CACHE);
        for (Person follower : activity.getPerson().getFollowers()) {
            LinkedList<Activity> streamList = streamCache.get(follower.getId());
            if (streamList != null) {
                synchronized (streamList) {
                    int index = streamList.indexOf(activity);
                    if (index != -1) {
                        streamList.set(index, activity);
                    }
                }
            }
        }

        // Refresh in profile activities
        Map<Long, LinkedList<Activity>> profileCache = caches.get(PROFILE_CACHE);
        LinkedList<Activity> profileList = profileCache.get(activity.getPerson().getId());
        if (profileList != null) {
            synchronized (profileList) {
                int index = profileList.indexOf(activity);
                if (index != -1) {
                    profileList.set(index, activity);
                }
            }
        }

    }

    /**
     * @see es.workast.service.activity.broker.ActivitySubscriber#acceptActivity(es.workast.model.activity.Activity)
     */
    public void acceptActivity(Activity activity) {

        // TODO: refactorizar

        // Include in global
        Map<Long, LinkedList<Activity>> globalCache = caches.get(GLOBAL_CACHE);
        LinkedList<Activity> globalList = globalCache.get(0L);
        if (globalList != null) {
            synchronized (globalList) {
                globalList.addFirst(activity);
                globalList.pollLast();
            }
        }

        // Include in group activities
        if (activity.getGroup() != null) {
            Map<Long, LinkedList<Activity>> groupCache = caches.get(GROUP_CACHE);
            LinkedList<Activity> groupList = groupCache.get(activity.getGroup().getId());
            if (groupList != null) {
                synchronized (groupList) {
                    groupList.addFirst(activity);
                    groupList.pollLast();
                }
            }
        }

        // Include in follower streams
        Map<Long, LinkedList<Activity>> streamCache = caches.get(STREAM_CACHE);
        for (Person follower : activity.getPerson().getFollowers()) {
            LinkedList<Activity> streamList = streamCache.get(follower.getId());
            if (streamList != null) {
                synchronized (streamList) {
                    streamList.addFirst(activity);
                    streamList.pollLast();
                }
            }
        }

        // Include in profile activities
        Map<Long, LinkedList<Activity>> profileCache = caches.get(PROFILE_CACHE);
        LinkedList<Activity> profileList = profileCache.get(activity.getPerson().getId());
        if (profileList != null) {
            synchronized (profileList) {
                profileList.addFirst(activity);
                profileList.pollLast();
            }
        }
    }

    /**
     * @see es.workast.service.activity.cache.ActivityCache#cleanCache()
     */
    //@RequestMapping(value = "clean")
    public void cleanCache() {
        caches = new HashMap<Long, Map<Long, LinkedList<Activity>>>();
        addCache(GLOBAL_CACHE);
        addCache(GROUP_CACHE);
        addCache(PROFILE_CACHE);
        addCache(STREAM_CACHE);
    }

}
