package es.workast.model.person;

import java.io.Serializable;
import java.util.Date;
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
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import es.workast.model.activity.Activity;
import es.workast.model.group.Group;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "Person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------- Properties

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PersonSequence")
    @SequenceGenerator(name = "PersonSequence", sequenceName = "PersonSequence")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastName1")
    private String lastName1;

    @Column(name = "lastName2")
    private String lastName2;

    @Column(name = "title")
    private String title;

    @Column(name = "area")
    private String area;

    @Column(name = "city")
    private String city;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "email")
    private String email;

    @Column(name = "hasPicture")
    private boolean hasPicture = false;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Followers", joinColumns = @JoinColumn(name = "personId"), inverseJoinColumns = @JoinColumn(name = "followerId"))
    @Cascade( { org.hibernate.annotations.CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Person> followers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Followers", joinColumns = @JoinColumn(name = "followerId"), inverseJoinColumns = @JoinColumn(name = "personId"))
    @Cascade( { org.hibernate.annotations.CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Person> following;

    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cascade( { org.hibernate.annotations.CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Group> groups;

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinColumn(name = "currentActivityId")
    private Activity currentActivity;

    @Column(name = "created")
    @Type(type = "es.workast.utils.PersistentDateTimeFromString")
    private DateTime created = new DateTime(DateTimeZone.forID("UTC"));

    @Column(name = "sendCommentsMail")
    private boolean sendCommentsMail = true;

    @Column(name = "sendDiggestMail")
    private boolean sendDiggestMail = true;

    // ---------- Methods

    public String getDisplayName() {
        return getName() + " " + getLastName1() + (getLastName2() == null ? "" : " " + getLastName2());
    }

    public String getCurrentActivityTitle() {
        return currentActivity == null ? null : currentActivity.getRenderedMessage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Person person = (Person) o;

        if (person.getId() != this.getId()) {
            return false;
        }

        return true;
    }

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

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String apellido1) {
        lastName1 = apellido1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String apellido2) {
        lastName2 = apellido2;
    }

    public Set<Person> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Person> following) {
        this.following = following;
    }

    public Set<Person> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Person> followers) {
        this.followers = followers;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public boolean isSendCommentsMail() {
        return sendCommentsMail;
    }

    public void setSendCommentsMail(boolean sendCommentsMail) {
        this.sendCommentsMail = sendCommentsMail;
    }

    public boolean isSendDiggestMail() {
        return sendDiggestMail;
    }

    public void setSendDiggestMail(boolean sendDiggestMail) {
        this.sendDiggestMail = sendDiggestMail;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
