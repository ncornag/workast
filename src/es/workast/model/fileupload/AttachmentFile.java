package es.workast.model.fileupload;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "AttachmentFile")
public class AttachmentFile implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------- Properties

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "FileSequence")
    @SequenceGenerator(name = "FileSequence", sequenceName = "FileSequence")
    private Long id;

    @Column(name = "created")
    @Type(type = "es.workast.utils.PersistentDateTimeFromString")
    private DateTime created = new DateTime(DateTimeZone.forID("UTC"));

    @Column(name = "path")
    private String path;

    @Column(name = "name")
    private String name;

    @Column(name = "contentType")
    private String contentType;

    // ---------- Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
