package es.workast.model.group;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import es.workast.model.person.Person;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Entity
@FilterDef(name = "person", parameters = @ParamDef(name = "personId", type = "long"))
@Table(name = "Groups")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------- Properties

    public static final long GLOBAL_GROUP_ID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GroupSequence")
    @SequenceGenerator(name = "GroupSequence", sequenceName = "GroupSequence")
    private Long id;

    @Column(name = "name")
    private String name;

    @Filter(name = "person", condition = "id = :personId")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "GroupPerson", joinColumns = @JoinColumn(name = "groupId"), inverseJoinColumns = @JoinColumn(name = "personId"))
    @Cascade( { org.hibernate.annotations.CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Person> members;

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinColumn(name = "adminId")
    private Person admin;

    @Column(name = "created")
    @Type(type = "es.workast.utils.PersistentDateTimeFromString")
    private DateTime created = new DateTime(DateTimeZone.forID("UTC"));

    @Column(name = "private")
    private Boolean priv;

    @Column(name = "listed")
    private Boolean listed;

    // ---------- Methods

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Group group = (Group) o;

        if (group.getId() != this.getId()) {
            return false;
        }

        return true;
    }

    // ---------- Accessors

    public Person getAdmin() {
        return admin;
    }

    public void setAdmin(Person admin) {
        this.admin = admin;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

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

    public Set<Person> getMembers() {
        if (members == null) {
            members = new HashSet<Person>();
        }
        return members;
    }

    public void setMembers(Set<Person> members) {
        this.members = members;
    }

    public Boolean getPriv() {
        return priv;
    }

    public void setPriv(Boolean priv) {
        this.priv = priv;
    }

    public Boolean getListed() {
        return listed;
    }

    public void setListed(Boolean listed) {
        this.listed = listed;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
