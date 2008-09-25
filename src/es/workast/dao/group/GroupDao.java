package es.workast.dao.group;

import java.util.Collection;
import java.util.List;

import es.workast.core.persistence.AbstractDao;
import es.workast.model.group.Group;
import es.workast.model.person.Person;
import es.workast.web.group.GroupStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface GroupDao extends AbstractDao<Group, Long> {

    /**
     * @param query Query string to filter by name
     * @param person Person to filter thr members collection
     * @return
     */
    List<GroupStatistic> getGroupStatistics(String query, Person person, boolean onlyPersonGroups);

    /**
     * @param name
     * @return
     */
    Collection<Group> findByName(String name);

}
