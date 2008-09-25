package es.workast.service.tag;

import java.util.List;

import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.model.tag.Tag;
import es.workast.web.tag.TagStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public interface TagManager {

    /**
     * @param person
     * @param tagPrefix
     * @return
     */
    List<Tag> getTagsByPerson(Person person, String tagPrefix);

    /**
     * @param activity
     * @param tagPrefix
     * @return
     */
    List<Tag> getTagsByActivity(Activity activity, String tagPrefix);

    /**
     * @param name
     * @return
     */
    Tag getTagByName(String name);

    /**
     * @param tag
     */
    void saveTag(Tag tag);

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