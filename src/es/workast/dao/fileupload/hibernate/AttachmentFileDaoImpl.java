package es.workast.dao.fileupload.hibernate;

import org.springframework.stereotype.Repository;

import es.workast.core.persistence.hibernate.AbstractHibernateDao;
import es.workast.dao.fileupload.AttachmentFileDao;
import es.workast.model.fileupload.AttachmentFile;

/**
 * TODO Documentar
 * 
 * @author Nicol�s Cornaglia
 */
@Repository("attachmentFileDao")
public class AttachmentFileDaoImpl extends AbstractHibernateDao<AttachmentFile, Long> implements AttachmentFileDao {

}
