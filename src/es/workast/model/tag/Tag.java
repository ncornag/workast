package es.workast.model.tag;

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

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "Tag")
public class Tag implements Serializable {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    // ---------- Constructors

    public Tag() {

    }

    public Tag(String name) {
        this.name = name;
    }

    // ---------- Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TagSequence")
    @SequenceGenerator(name = "TagSequence", sequenceName = "TagSequence")
    private Long id;

    @Column(name = "name")
    private String name;

    // ---------- Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
