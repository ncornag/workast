package es.workast.activity.persistence;

import java.util.List;

import es.workast.activity.Activity;
import es.workast.core.persistence.AbstractDao;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface ActivityDao extends AbstractDao<Activity, Long> {

    /**
     * @param personId
     * @param groupId
     * @param min
     * @param size
     * @return
     */
    public List<Activity> getActivities(Long personId, Long groupId, int min, int size);

}
