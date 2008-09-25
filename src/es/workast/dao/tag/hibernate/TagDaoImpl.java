package es.workast.dao.tag.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import es.workast.core.persistence.hibernate.AbstractHibernateDao;
import es.workast.dao.tag.TagDao;
import es.workast.model.activity.Activity;
import es.workast.model.person.Person;
import es.workast.model.tag.Tag;
import es.workast.web.tag.TagStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Repository("tagDao")
public class TagDaoImpl extends AbstractHibernateDao<Tag, Long> implements TagDao {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * @see es.workast.dao.tag.TagDao#getTagsByPerson(es.workast.model.person.Person, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Tag> getTagsByPerson(Person person, String tagPrefix) throws DataAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getTagsByPerson - person: [" + person.getId() + "] tagPrefix [" + tagPrefix + "]");
        }
        return getHibernateTemplate().findByNamedParam("select distinct at.tag from ActivityTag as at where at.person = :person and at.tag.name like :name", //--
                new String[] { "person", "name" }, new Object[] { person, tagPrefix + "%" });
    }

    /**
     * @see es.workast.dao.tag.TagDao#getTagsByActivity(es.workast.model.activity.Activity, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Tag> getTagsByActivity(Activity activity, String tagPrefix) {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getTagsByActivity - activity: [" + activity.getId() + "] tagPrefix [" + tagPrefix + "]");
        }
        return getHibernateTemplate().findByNamedParam("select distinct at.tag from ActivityTag as at where at.activity = :activity and at.tag.name like :name", //--
                new String[] { "activity", "name" }, new Object[] { activity, tagPrefix + "%" });
    }

    /**
     * @see es.workast.dao.tag.TagDao#getTagByName(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Tag getTagByName(String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getTagByName - name: [" + name + "]");
        }
        List<Tag> tags = getHibernateTemplate().findByNamedParam("from Tag t where t.name = :name", "name", name);
        if (tags.size() == 0) {
            return null;
        } else if (tags.size() > 1) {
            throw new DataIntegrityViolationException("Duplicate tag: " + name);
        } else {
            return tags.get(0);
        }
    }

    /**
     * @see es.workast.dao.tag.TagDao#getTagStatisticsByPerson(es.workast.model.person.Person)
     */
    @SuppressWarnings("unchecked")
    public List<TagStatistic> getTagStatisticsByPerson(Person person) {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getTagStatisticsByPerson [" + person.getId() + "]");
        }
        List<Object[]> results = getHibernateTemplate().find("select t.name, count(*) from Tag t, ActivityTag at where at.tag=t and at.person=? group by t.name", person);
        List<TagStatistic> tagStats = new ArrayList<TagStatistic>(results.size());

        for (Object[] result : results) {
            String name = (String) result[0];
            int count = ((Number) result[1]).intValue();
            tagStats.add(new TagStatistic(name, count));
        }
        return tagStats;
    }

    /**
     * @see es.workast.dao.tag.TagDao#getTagStatistics()
     */
    @SuppressWarnings("unchecked")
    public List<TagStatistic> getTagStatistics() {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getTagStatistics");
        }
        List<Object[]> results = getHibernateTemplate().find("select t.name, count(*) from Tag t, ActivityTag at where at.tag=t group by t.name");
        List<TagStatistic> tagStats = new ArrayList<TagStatistic>(results.size());

        for (Object[] result : results) {
            String name = (String) result[0];
            int count = ((Number) result[1]).intValue();
            tagStats.add(new TagStatistic(name, count));
        }
        return tagStats;
    }

}
