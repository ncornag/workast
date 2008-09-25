package es.workast.service.group;

import java.util.List;

import es.workast.model.group.Group;
import es.workast.web.group.GroupStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface GroupManager {

    String GROUPNOTFOUND = "Group not found.";
    String BIND_ERROR = "Cannot bind properties.";

    /**
     * Saves a Group bean
     * 
     * @param group
     */
    void saveGroup(Group group);

    /**
     * Gets a Group by id
     * 
     * @param id
     * @return
     */
    Group getGroup(Long groupId);

    /**
     * Gets a list of GroupStatistics for Groups with names starting with 'name'
     * 
     * @return
     */
    List<GroupStatistic> getGroupsByName(String name);

    /**
     * Creates a new Group, an Activity, and returns the new Group id
     * 
     * @return
     */
    Group createGroup(Group group);

}
