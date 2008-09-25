package es.workast.dao.person;

import es.workast.core.persistence.AbstractDao;
import es.workast.model.person.PendingPassword;
import es.workast.model.person.Person;
import java.util.Date;
import java.util.List;

public interface PendingPasswordDao extends AbstractDao<PendingPassword, Long> {

    List<PendingPassword> findAllByPersonId(Long personId);

    Person getPersonByPendingUID(String uid);

    int deleteAllPendingPassword(Long personId);

    int deletePendingPasswordSince(Date since);

    int deletePendingPasswordSinceLastMonth();
    
}