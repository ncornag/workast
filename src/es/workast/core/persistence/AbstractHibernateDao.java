package es.workast.core.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Nicolás Cornaglia
 */
@SuppressWarnings("unchecked")
public abstract class AbstractHibernateDao<DomainObject extends Serializable, KeyType extends Serializable> extends HibernateDaoSupport {

    protected Class<DomainObject> domainClass = getDomainClass();

    protected Class<DomainObject> getDomainClass() {
        Object type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            return (Class<DomainObject>) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            return (Class<DomainObject>) type.getClass();
        }
    }

    // Hibernate default implementations

    public DomainObject get(KeyType id) {
        return (DomainObject) getHibernateTemplate().get(domainClass, id);
    }

    public DomainObject load(KeyType id) {
        return (DomainObject) getHibernateTemplate().load(domainClass, id);
    }

    public void update(DomainObject t) {
        getHibernateTemplate().update(t);
    }

    public void addUpdate(DomainObject t) {
        getHibernateTemplate().merge(t);
    }

    public KeyType save(DomainObject t) {
        return (KeyType) getHibernateTemplate().save(t);
    }

    public void saveOrUpdate(DomainObject t) {
        getHibernateTemplate().saveOrUpdate(t);
    }

    public void delete(DomainObject t) {
        getHibernateTemplate().delete(t);
    }

    public void deleteById(KeyType id) {
        Object obj = load(id);
        getHibernateTemplate().delete(obj);
    }

    public List<DomainObject> findAll(boolean cacheable) {
        return getSession().createQuery("from " + domainClass.getName()).setCacheable(cacheable).list();
    }

    public List<DomainObject> findAll() {
        return findAll(true);
    }

}