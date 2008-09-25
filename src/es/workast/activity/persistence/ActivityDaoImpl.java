package es.workast.activity.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.workast.activity.Activity;
import es.workast.core.persistence.AbstractHibernateDao;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Repository("activityDao")
public class ActivityDaoImpl extends AbstractHibernateDao<Activity, Long> implements ActivityDao {

    /**
     * @see es.workast.activity.persistence.ActivityDao#getActivities(java.lang.Long, java.lang.Long, int, int)
     */
    @SuppressWarnings("unchecked")
    public List<Activity> getActivities(Long personId, Long groupId, int min, int size) {
        //        return getSession().createCriteria(Activity.class) // --
        //                .setCacheable(true) // --
        //                .addOrder(Order.desc("postedTime")) // --
        //                .setFirstResult(min) // --
        //                .setFetchSize(size) // --
        //                .list();
        return getSession() //--
                .createQuery("from Activity a where a.group.id = :groupId and a.person.id = :personId order by postedTime desc") //--
                .setParameter("personId", personId) // --
                .setParameter("groupId", groupId) // --
                .setCacheable(true) //--
                .setFirstResult(min) //--
                .setMaxResults(size) //--
                .list();
        //    "from Activity a where a.group.id = :groupId and (a.person.id = :personId or a.person.id in (select f.personId from followers f where f.followerId=:personId)) order by postedTime desc") //--
    }
}
