package es.workast.core.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * @author Nicolás Cornaglia
 */
public interface AbstractDao<DomainObject extends Serializable, KeyType extends Serializable> {

    DomainObject get(KeyType id);

    DomainObject load(KeyType id);

    void update(DomainObject object);

    void addUpdate(DomainObject t);

    KeyType save(DomainObject object);

    void saveOrUpdate(DomainObject object);

    void delete(DomainObject object);

    void deleteById(KeyType id);

    List<DomainObject> findAll(boolean cacheable);

    List<DomainObject> findAll();

}