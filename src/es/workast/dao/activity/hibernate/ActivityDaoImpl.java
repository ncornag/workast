package es.workast.dao.activity.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import es.workast.core.persistence.hibernate.AbstractHibernateDao;
import es.workast.dao.activity.ActivityDao;
import es.workast.model.activity.Activity;
import es.workast.model.activity.type.comment.CommentActivityType;
import es.workast.model.activity.type.event.EventActivityType;
import es.workast.model.activity.type.status.StatusActivityType;
import es.workast.model.activity.type.status.StatusActivityTypeImpl;
import es.workast.model.person.Person;
import es.workast.web.stream.StreamFilter;

/**
 * TODO Documentar
 * 
 * Streams:
 * 
 * Global: Todas las actividades públicas (del grupo Group.GLOBAL_GROUP_ID)
 * MyStream: Todos las actividades publicas de las personas que el personId sigue y está suscrito + las actividades de mis grupos
 * Grupo: Todas las actividades públicas de un grupo específico
 * Ajeno: Todas las actividades públicas de una persona
 * Personal: Todas las actividades de una persona, tanto públicas como privadas
 * 
 * @author Nicolás Cornaglia
 */
@Repository("activityDao")
public class ActivityDaoImpl extends AbstractHibernateDao<Activity, Long> implements ActivityDao {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * @see es.workast.dao.activity.ActivityDao#getPersonStreamActivities(Person, StreamFilter)
     */
    @SuppressWarnings("unchecked")
    public List<Activity> getPersonStreamActivities(Person person, StreamFilter streamFilter) {
        Criteria q = getActivityBaseCriteria(person, person, streamFilter);
        q.add(Restrictions.eq("personPrivate", false));

        q.add(Restrictions.or( //--
                Restrictions.sqlRestriction( //--
                        "{alias}.personId in (select personId from followers where followerId = ?)", person.getId(), Hibernate.LONG), //--
                Restrictions.sqlRestriction( //--
                        "{alias}.groupId in (select groupId from groupperson where personId = ?)", person.getId(), Hibernate.LONG)));

        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getPersonStreamActivities - person: [" + person.getId() + "] id: [" + (streamFilter.getBackwards() ? "<" : ">") + streamFilter.getFirstId()
                    + "]");
        }
        //return q.list();
        return doFinalQuery(q, person);
    }

    /**
     * @see es.workast.dao.activity.ActivityDao#getGlobalActivities(es.workast.model.person.Person, es.workast.web.stream.StreamFilter)
     */
    @SuppressWarnings("unchecked")
    public List<Activity> getGlobalActivities(Person person, StreamFilter streamFilter) {
        Criteria q = getActivityBaseCriteria(person, person, streamFilter);
        q.add(Restrictions.eq("personPrivate", false));

        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getGlobalActivities - id: [" + (streamFilter.getBackwards() ? "<" : ">") + streamFilter.getFirstId() + "]");
        }
        //return q.list();
        return doFinalQuery(q, person);
    }

    /**
     * @see es.workast.dao.activity.ActivityDao#getGroupActivities(Person, Long, StreamFilter)
     */
    @SuppressWarnings("unchecked")
    public List<Activity> getGroupActivities(Person person, Long groupId, StreamFilter streamFilter) {
        Criteria q = getActivityBaseCriteria(person, person, streamFilter);
        q.add(Restrictions.eq("group.id", groupId)) // --
                .add(Restrictions.eq("personPrivate", false));

        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getGroupActivities - group: [" + groupId + "] id: [" + (streamFilter.getBackwards() ? "<" : ">") + streamFilter.getFirstId() + "]");
        }
        //return q.list();
        return doFinalQuery(q, person);
    }

    /**
     * @see es.workast.dao.activity.ActivityDao#getProfileStreamActivities(Person, Person, StreamFilter)
     */
    @SuppressWarnings("unchecked")
    public List<Activity> getProfileStreamActivities(Person person, Person tagsPerson, StreamFilter streamFilter) {
        Criteria q = getActivityBaseCriteria(person, tagsPerson, streamFilter);
        q.add(Restrictions.eq("personPrivate", false)) // --
                .add(Restrictions.eq("person.id", person.getId()));

        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getProfileStreamActivities - person: [" + person.getId() + "] id: [" + (streamFilter.getBackwards() ? "<" : ">") + streamFilter.getFirstId()
                    + "]");
        }
        //return q.list();
        return doFinalQuery(q, tagsPerson);
    }

    /**
     * Get the ids and get the activities
     * 
     * @param q
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Activity> doFinalQuery(Criteria q, Person tagsPerson) {
        q.setProjection(Projections.distinct(Projections.projectionList().add(Projections.id())));
        Object[] list = (q.list()).toArray();

        if (list.length == 0) {
            return new ArrayList();
        }

        getSession().enableFilter("person").setParameter("personId", tagsPerson.getId());

        //        Criteria finalQuery = getSession().createCriteria(Activity.class);
        //        finalQuery.addOrder(Order.desc("postedTime"));
        //        finalQuery.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        //        finalQuery.add(Restrictions.in("id", list));
        // 
        //        List<Activity> results = finalQuery.list();

        String hql = "from Activity where id in(:ids) order by postedTime desc";
        Query hQuery = getSession().createQuery(hql).setParameterList("ids", list);
        getSession().enableFilter("person").setParameter("personId", tagsPerson.getId());

        List<Activity> results = hQuery.list();

        return results;
    }

    /**
     * Build the base Stream Criteria
     * 
     * @param person
     * @param streamFilter
     * @return
     */
    private Criteria getActivityBaseCriteria(Person person, Person tagsPerson, StreamFilter streamFilter) {
        Criteria q = getSession().createCriteria(Activity.class, "act") // --
                .setCacheable(false) // --
                .setFetchMode("tags", FetchMode.JOIN) //--
                .addOrder(Order.desc("act.id")) //--postedTime
                .setMaxResults(streamFilter.getMaxResults().intValue()) // --
                .add(streamFilter.getBackwards() ? Restrictions.le("id", streamFilter.getFirstId()) : Restrictions.gt("id", streamFilter.getFirstId()));

        String type = streamFilter.getType().toUpperCase();
        if (!"".equals(type)) {
            if (type.equals(StatusActivityTypeImpl.TYPE)) {
                q.add(Restrictions.or(Restrictions.eq("type", StatusActivityType.TYPE), Restrictions.eq("type", CommentActivityType.TYPE)));
            } else {
                q.add(Restrictions.eq("type", EventActivityType.TYPE));
            }
        }
        if (!"".equals(streamFilter.getText())) {
            q.add(Restrictions.ilike("message", "%" + streamFilter.getText() + "%"));
        }
        if (streamFilter.getTags() != null) {
            int offSet = 1 + (streamFilter.isGlobalTags() ? 0 : 1);
            Object[] values = new Object[streamFilter.getTags().length + offSet];
            Type[] types = new Type[streamFilter.getTags().length + offSet];
            values[0] = streamFilter.getTags().length;
            types[0] = Hibernate.INTEGER;
            if (!streamFilter.isGlobalTags()) {
                values[1] = tagsPerson.getId();
                types[1] = Hibernate.LONG;
            }
            String list = "";
            for (int i = 0; i < streamFilter.getTags().length; i++) {
                values[i + offSet] = streamFilter.getTags()[i];
                types[i + offSet] = Hibernate.STRING;
                list += ",?";
            }
            list = list.substring(1);

            q.add(Restrictions.sqlRestriction("? = (select count(*) from ActivityTag at, Tag t where " + (streamFilter.isGlobalTags() ? "" : "at.personId=? and ")
                    + " at.tagId = t.id and at.activityid = {alias}.id and t.name in (" + list + "))", values, types));
        }
        getSession().enableFilter("person").setParameter("personId", tagsPerson.getId());

        return q;
    }
}
