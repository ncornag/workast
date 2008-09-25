package es.workast.dao.tag;

import java.util.List;

import es.workast.core.persistence.AbstractDao;
import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.model.tag.Tag;
import es.workast.web.tag.TagStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface TagDao extends AbstractDao<Tag, Long> {

    /**
     * Gets a list of  the tags used by the person, filtered by prefix (LIKE 'tagPrefix%')
     * 
     * @param person
     * @param tagPrefix
     * @return
     */
    List<Tag> getTagsByPerson(Person person, String tagPrefix);

    /**
     * @param name
     * @return
     */
    Tag getTagByName(String name);

    /**
     * @param activity
     * @param tagPrefix
     * @return
     */
    List<Tag> getTagsByActivity(Activity activity, String tagPrefix);

    /**
     * @param person
     * @return
     */
    List<TagStatistic> getTagStatisticsByPerson(Person person);

    /**
     * @param person
     * @return
     */
    List<TagStatistic> getTagStatistics();

}
