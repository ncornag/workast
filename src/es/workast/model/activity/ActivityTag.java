package es.workast.model.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import es.workast.model.person.Person;
import es.workast.model.tag.Tag;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "ActivityTag")
public class ActivityTag {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    // ---------- Constructors

    public ActivityTag() {

    }

    public ActivityTag(Activity activity, Person person, Tag tag) {
        this.activity = activity;
        this.person = person;
        this.tag = tag;
    }

    // ---------- Properties

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ActivityTagSequence")
    @SequenceGenerator(name = "ActivityTagSequence", sequenceName = "ActivityTagSequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activityId")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;

    // ---------- Accessors

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

}
