package es.workast.service.tag.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.workast.dao.tag.TagDao;
import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.model.tag.Tag;
import es.workast.service.tag.TagManager;
import es.workast.web.tag.TagStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service("tagManager")
public class TagManagerImpl implements TagManager {

    // ---------- Services

    @Autowired
    private TagDao tagDao;

    // ---------- Methods

    /**
     * @see es.workast.service.tag.TagManager#getTagsByPerson(es.workast.model.person.Person, java.lang.String)
     */
    public List<Tag> getTagsByPerson(Person person, String tagPrefix) {
        return tagDao.getTagsByPerson(person, tagPrefix);
    }

    /**
     * @see es.workast.service.tag.TagManager#getTagsByActivity(es.workast.model.activity.Activity, java.lang.String)
     */
    public List<Tag> getTagsByActivity(Activity activity, String tagPrefix) {
        return tagDao.getTagsByActivity(activity, tagPrefix);
    }

    /**
     * @see es.workast.service.tag.TagManager#getTagByName(java.lang.String)
     */
    public Tag getTagByName(String name) {
        return tagDao.getTagByName(name);
    }

    /**
     * @see es.workast.service.tag.TagManager#saveTag(es.workast.model.tag.Tag)
     */
    @Transactional(readOnly = false)
    public void saveTag(Tag tag) {
        tagDao.saveOrUpdate(tag);
    }

    /**
     * @see es.workast.service.tag.TagManager#getTagStatistics()
     */
    public List<TagStatistic> getTagStatistics() {
        return tagDao.getTagStatistics();
    }

    /**
     * @see es.workast.service.tag.TagManager#getTagStatisticsByPerson(es.workast.model.person.Person)
     */
    public List<TagStatistic> getTagStatisticsByPerson(Person person) {
        return tagDao.getTagStatisticsByPerson(person);
    }

    // ---------- Accessors

    public TagDao getTagDao() {
        return tagDao;
    }

    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

}
