package es.workast.dao.group.hibernate;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import es.workast.core.persistence.hibernate.AbstractHibernateDao;
import es.workast.dao.group.GroupDao;
import es.workast.model.group.Group;
import es.workast.model.person.Person;
import es.workast.web.group.GroupStatistic;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Repository("groupDao")
public class GroupDaoImpl extends AbstractHibernateDao<Group, Long> implements GroupDao {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * @see es.workast.dao.group.GroupDao#getGroupStatistics(java.lang.String, es.workast.model.person.Person, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<GroupStatistic> getGroupStatistics(String q, Person person, boolean onlyPersonGroups) {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: getGroupStatistics - q: [" + q + "]");
        }
        Query sql = getSession()
                .createSQLQuery(
                        "select g.id, g.name, g.created, g.private private, g.listed, g.adminId, p.name as adminName, p.lastName1 as adminLastName1, p.lastName2 as adminLastName2, "
                                + "(select count(*) from GroupPerson gp where gp.groupId=g.id and gp.personId=:personId) currentUserIsMember, (select count(*) from GroupPerson gp where gp.groupId=g.id) membersCount, count(ac.id) messagesCount "
                                + " from Groups g left outer join Person p on g.adminId=p.id left outer join Activity ac on g.id=ac.groupId "
                                + " where g.name like :q and (g.listed=true or (g.listed=false and g.adminId=:personId)) "
                                + (onlyPersonGroups ? " and exists(select 1 from GroupPerson gp where gp.groupId=g.id and gp.personId=:personId)"
                                        : "") + " group by g.id, g.name, g.created, g.private, g.listed, g.adminId, p.name, p.lastName1, p.lastName2") //--
                .setResultTransformer(Transformers.aliasToBean(GroupStatistic.class)) //--
                .setString("q", (q == null ? "" : q) + "%").setLong("personId", person.getId());

        ((SQLQuery) sql).addScalar("id", Hibernate.LONG).addScalar("name", Hibernate.STRING).addScalar("created", Hibernate.STRING).addScalar(
                "private", Hibernate.BOOLEAN).addScalar("listed", Hibernate.BOOLEAN).addScalar("adminId", Hibernate.LONG).addScalar("adminName",
                Hibernate.STRING).addScalar("adminLastName1", Hibernate.STRING).addScalar("adminLastName2", Hibernate.STRING).addScalar(
                "currentUserIsMember", Hibernate.BOOLEAN).addScalar("membersCount", Hibernate.LONG).addScalar("messagesCount", Hibernate.LONG);

        return sql.list();
    }

    /**
     * @see es.workast.dao.group.GroupDao#findByName(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Collection<Group> findByName(String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("DAO called: findByName - name: [" + name + "]");
        }
        String trimmedName = name.trim().toUpperCase();
        return getHibernateTemplate().findByNamedParam("from Group g where UPPER(g.name) like :n order by g.name", "n", trimmedName + '%');
    }

}
