package es.workast.model.activity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import es.workast.model.group.Group;
import es.workast.model.person.Person;
import es.workast.model.tag.Tag;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Entity()
@FilterDef(name = "person", parameters = @ParamDef(name = "personId", type = "long"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "Activity")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------- Constructors

    public Activity() {

    }

    public Activity(Person person, Group group, String type) {
        this.person = person;
        this.group = group;
        this.type = type;
    }

    // ---------- Properties

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ActivitySequence")
    @SequenceGenerator(name = "ActivitySequence", sequenceName = "ActivitySequence")
    private Long id;

    @Column(name = "postedTime")
    @Type(type = "es.workast.utils.PersistentDateTimeFromString")
    private DateTime postedTime = new DateTime(DateTimeZone.forID("UTC"));

    @Column(name = "updatedTime")
    @Type(type = "es.workast.utils.PersistentDateTimeFromString")
    private DateTime updatedTime = postedTime;

    @Column(name = "activityType")
    private String type;

    @Filter(name = "person", condition = "personId = :personId")
    @OneToMany(mappedBy = "activity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cascade( { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ActivityTag> tags = new HashSet<ActivityTag>();

    // Comments
    @OneToMany(mappedBy = "parentActivity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cascade( { org.hibernate.annotations.CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Activity> comments = new HashSet<Activity>();

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinColumn(name = "personId")
    private Person person;

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinColumn(name = "groupId")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "inReplyToId")
    private Activity replyToActivity;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Activity parentActivity;

    @Column(name = "personPrivate")
    private boolean personPrivate = false;

    @Column(name = "personDeleted")
    private boolean personDeleted = false;

    @Column(name = "message")
    private String message;

    @Column(name = "renderedMessage")
    private String renderedMessage;

    @Column(name = "data")
    @Type(type = "es.workast.dao.activity.hibernate.ActivityDataUserType")
    private ActivityData activityData;

    // ---------- Methods

    public Activity addRelated(Activity activity) {
        activity.setReplyToActivity(this);
        if (this.getParentActivity() != null) {
            activity.setParentActivity(getParentActivity());
            getParentActivity().getComments().add(activity);
            getParentActivity().setUpdatedTime(activity.getPostedTime());
        } else {
            activity.setParentActivity(this);
            comments.add(activity);
            setUpdatedTime(activity.getPostedTime());
        }
        return this;
    }

    public Activity addTag(Person person, Tag tag) {
        boolean exists = false;
        for (ActivityTag activityTag : tags) {
            if (activityTag.equals(tag)) {
                exists = true;
            }
        }
        if (!exists) {
            ActivityTag activityTag = new ActivityTag(this, person, tag);
            tags.add(activityTag);
        }
        return this;
    }

    public Activity removeTag(Person person, Tag tag) {
        ActivityTag theActivityTag = null;
        for (ActivityTag activityTag : tags) {
            if (activityTag.getTag().equals(tag)) {
                theActivityTag = activityTag;
                break;
            }
        }
        if (theActivityTag != null) {
            tags.remove(theActivityTag);
        }
        return this;
    }

    public boolean hasTag(Tag tag) {
        for (ActivityTag activityTag : tags) {
            if (tag.equals(activityTag.getTag())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Activity) {
            Activity that = (Activity) other;
            result = (this.getId().equals(that.getId()));
        }
        return result;
    }

    @Override
    public int hashCode() {
        return id == null ? super.hashCode() : getId().hashCode();
    }

    // ---------- Additional data

    public Long getParentId() {
        return parentActivity == null ? null : parentActivity.getId();
    }

    public Long getReplyToOwnerId() {
        return replyToActivity == null ? null : replyToActivity.getPerson().getId();
    }

    public String getReplyToOwnerName() {
        return replyToActivity == null ? null : replyToActivity.getPerson().getName();
    }

    public String getReplyToRenderedMessage() {
        return replyToActivity == null ? null : replyToActivity.getRenderedMessage();
    }

    public Long getGroupId() {
        return getGroup().getId();
    }

    public String getGroupName() {
        return getGroup().getName();
    }

    // ---------- Accessors

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

    public Set<ActivityTag> getTags() {
        return tags;
    }

    public void setTags(Set<ActivityTag> tags) {
        this.tags = tags;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isPersonPrivate() {
        return personPrivate;
    }

    public void setPersonPrivate(boolean personPrivate) {
        this.personPrivate = personPrivate;
    }

    public boolean isPersonDeleted() {
        return personDeleted;
    }

    public void setPersonDeleted(boolean personDeleted) {
        this.personDeleted = personDeleted;
    }

    public DateTime getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(DateTime postedTime) {
        this.postedTime = postedTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(DateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Activity getReplyToActivity() {
        return replyToActivity;
    }

    public void setReplyToActivity(Activity replyToActivity) {
        this.replyToActivity = replyToActivity;
    }

    public Activity getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public Set<Activity> getComments() {
        return comments;
    }

    public void setComments(Set<Activity> related) {
        this.comments = related;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRenderedMessage() {
        return renderedMessage;
    }

    public void setRenderedMessage(String renderedMessage) {
        this.renderedMessage = renderedMessage;
    }

    public ActivityData getActivityData() {
        return activityData;
    }

    public void setActivityData(ActivityData activityData) {
        this.activityData = activityData;
    }

}
