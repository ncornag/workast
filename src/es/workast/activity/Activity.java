package es.workast.activity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CollectionOfElements;

import es.workast.group.Group;
import es.workast.person.Person;
import es.workast.render.RenderEngine;
import es.workast.tag.Tag;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Entity
@Table(name = "Activity")
@XmlRootElement(name = "activity")
@XmlAccessorType(XmlAccessType.NONE)
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------- Properties

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    private Long id;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    @CollectionOfElements(fetch = FetchType.EAGER)
    @JoinTable(name = "ActivityParams", joinColumns = @JoinColumn(name = "activityId"))
    @Column(name = "paramValue", nullable = false)
    @org.hibernate.annotations.MapKey(columns = { @Column(name = "paramKey") })
    private Map<String, String> params;

    @Column(name = "postedTime")
    @XmlElement
    private Date postedTime;

    @Column(name = "activityType")
    @XmlElement
    private String type;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    @Transient
    private Set<Tag> tags;
    @Transient
    private boolean personPrivate;
    @Transient
    private boolean personDeleted;

    // ---------- Render properties

    @Transient
    private boolean wasRendered = false;

    @Transient
    @XmlElement
    private String title;
    @Transient
    @XmlElement
    private String body;
    @Transient
    @XmlElement
    private String iconUrl;

    // ---------- Methods

    /**
     * @param renderEngine
     * @param activityType
     * @throws Exception
     */
    public void render(RenderEngine renderEngine, ActivityType activityType) throws Exception {
        Map<String, Object> p = new HashMap<String, Object>(getParams());
        p.put("owner", getPerson());

        // Title...
        title = renderEngine.render(activityType.getTitleTemplate(), p);
        // Optional Body...
        String bodyTemplate = activityType.getBodyTemplate(p);
        if (bodyTemplate != null) {
            body = renderEngine.render(bodyTemplate, p);
        }
        // Icon...
        iconUrl = activityType.getIconImage();

        // Mark as rendered
        wasRendered = true;
    }

    /**
     * @return
     */
    public boolean wasRendered() {
        return wasRendered;
    }

    /**
     * @return
     */
    public boolean hasBody() {
        return body != null;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
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

    public Date getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(Date postedTime) {
        this.postedTime = postedTime;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getIconUrl() {
        return iconUrl;
    }

}
