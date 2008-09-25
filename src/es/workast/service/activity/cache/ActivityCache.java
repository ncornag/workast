package es.workast.service.activity.cache;

import java.util.List;

import es.workast.model.activity.Activity;

/**
 * @author Nicolás Cornaglia
 */
public interface ActivityCache {

    public Long GLOBAL_CACHE = 1L;
    public Long GROUP_CACHE = 2L;
    public Long PROFILE_CACHE = 3L;
    public Long STREAM_CACHE = 4L;

    /**
     * @param cacheId
     */
    void addCache(Long cacheId);

    /**
     * @param cacheId
     * @param listId
     * @return
     */
    List<Activity> getCachedList(Long cacheId, Long listId);

    /**
     * @param cacheId
     * @param listId
     * @param list
     * @return
     */
    List<Activity> setCachedList(Long cacheId, Long listId, List<Activity> list);

    /**
     * Updates an {@link Activity} in cache
     * 
     * @param activity
     */
    void refreshActivity(Activity activity);

    /**
     * Cleans the cache
     */
    void cleanCache();

}
