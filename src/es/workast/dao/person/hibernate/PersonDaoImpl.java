package es.workast.dao.person.hibernate;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.joda.time.ReadableDateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import es.workast.core.persistence.hibernate.AbstractHibernateDao;
import es.workast.dao.person.PersonDao;
import es.workast.model.person.Person;
import es.workast.utils.DatabaseUtils;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Repository("personDao")
public class PersonDaoImpl extends AbstractHibernateDao<Person, Long> implements PersonDao {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * @see es.workast.dao.person.PersonDao#getPersonByUsername(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Person getPersonByUsername(String username) throws DataAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getPersonByUsername - username: [" + username + "]");
        }
        List<Person> users = getHibernateTemplate().findByNamedParam("from Person p where p.email = :username", "username", username);
        if (users.size() == 0) {
            return null;
        } else if (users.size() > 1) {
            throw new DataIntegrityViolationException("Duplicate user: " + username);
        } else {
            return users.get(0);
        }
    }

    /**
     * @see es.workast.dao.person.PersonDao#findByName(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Collection<Person> findByName(String query) {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: findByName - query: [" + query + "]");
        }
        return getHibernateTemplate().findByNamedParam("from Person p where p.name like :q or p.lastName1 like :q or p.lastName2 like :q order by p.name", "q", query + '%');
    }

    /**
     * @see es.workast.dao.person.PersonDao#getStatistics(es.workast.model.person.Person, java.util.Date)
     */
    @SuppressWarnings("unchecked")
    public List getStatistics(Person person, ReadableDateTime fromDate) {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getStatistics - person: [" + person.getId() + "]");
        }
        String sql = "select ${postedDate} as ad, count(*) from Activity a where a.personId=:personId and a.postedTime>=:fromDate group by ad order by ad";
        String hibernateDialect = DatabaseUtils.getDialect();
        String replacement = "DATE(a.postedTime)";
        if ("org.hibernate.dialect.HSQLDialect".equals(hibernateDialect)) {
            replacement = "CONVERT(LEFT(CONVERT(a.postedTime, varchar(30)),10), DATE)";
        }
        sql = sql.replace("${postedDate}", replacement);
        Query query = getSession().createSQLQuery(sql) //--
                .setLong("personId", person.getId()) //--
                .setDate("fromDate", fromDate.toDateTime().toDate());
        return query.list();
    }

    public void flushAndRefresh(Person object) {
        getSessionFactory().getCurrentSession().flush();
        getSessionFactory().getCurrentSession().refresh(object);
    }
}
