package es.workast.dao.person.hibernate;

import es.workast.core.persistence.hibernate.AbstractHibernateDao;
import es.workast.dao.person.PendingPasswordDao;
import es.workast.model.person.PendingPassword;
import es.workast.model.person.Person;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sergio R. Gianazza
 */

@Repository("pendingPasswordDao")
public class PendingPasswordDaoImpl extends AbstractHibernateDao<PendingPassword, Long> implements PendingPasswordDao {

    public Person getPersonByPendingUID(String uid) {
       Criteria q = getSession().createCriteria(PendingPassword.class).add(Restrictions.eq("uid", uid)).setMaxResults(1);
       PendingPassword pp = (PendingPassword) q.uniqueResult();
       if (pp == null) { return null;}
       return pp.getPerson();
    }

    public int deleteAllPendingPassword(Long personId) {
        return getSession().createQuery("delete from PendingPassword pending where pending.person.id=" + personId).executeUpdate();
    }

    public int deletePendingPasswordSince(Date since) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strSince = sdf.format(since);
        return getSession().createQuery("delete from PendingPassword pending where pending.insertedDate <= '" + strSince + "'").executeUpdate();
    }

    public List<PendingPassword> findAllByPersonId(Long personId) {
        Criteria q = getSession().createCriteria(PendingPassword.class).add(Restrictions.eq("person.id", personId));
        return q.list();
    }

    public int deletePendingPasswordSinceLastMonth() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return deletePendingPasswordSince(cal.getTime());
    }

}
