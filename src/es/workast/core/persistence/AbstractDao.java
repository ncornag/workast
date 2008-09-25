package es.workast.core.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * @author Nicolás Cornaglia
 */
public interface AbstractDao<DomainObject extends Serializable, KeyType extends Serializable> {

    public DomainObject get(KeyType id);

    public DomainObject load(KeyType id);

    public void update(DomainObject object);

    public void addUpdate(DomainObject t);

    public KeyType save(DomainObject object);

    public void saveOrUpdate(DomainObject object);

    public void delete(DomainObject object);

    public void deleteById(KeyType id);

    public List<DomainObject> findAll(boolean cacheable);

    public List<DomainObject> findAll();

}