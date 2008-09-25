package es.workast.model.activity.type.newgroup;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.workast.model.activity.ActivityData;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.NONE)
public class NewGroupActivityData implements ActivityData, Serializable {

    // ---------- Properties

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String type = NewGroupActivityTypeImpl.TYPE;

    @XmlElement
    private Long groupId;

    @XmlElement
    private String groupName;

    // ---------- Constructors

    public NewGroupActivityData() {
    }

    public NewGroupActivityData(Long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    // ---------- Accessors

    /**
     * @see es.workast.model.activity.ActivityData#getType()
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
