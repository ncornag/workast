package es.workast.web.group;

import org.joda.time.DateTime;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class GroupStatistic {

    private Long id;

    private String name;

    private DateTime created;

    private Boolean priv;

    private Boolean listed;

    private Long adminId;

    private String adminName;

    private String adminLastName1;

    private String adminLastName2;

    private Boolean currentUserIsMember;

    private Long membersCount;

    private Long messagesCount;

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

    public String getCreated() {
        return created.toString();
    }

    public void setCreated(String created) {
        this.created = new DateTime(created);
    }

    public Boolean getPrivate() {
        return priv;
    }

    public void setPrivate(Boolean priv) {
        this.priv = priv;
    }

    public Boolean getListed() {
        return listed;
    }

    public void setListed(Boolean listed) {
        this.listed = listed;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminLastName1() {
        return adminLastName1;
    }

    public void setAdminLastName1(String adminLastName1) {
        this.adminLastName1 = adminLastName1;
    }

    public String getAdminLastName2() {
        return adminLastName2;
    }

    public void setAdminLastName2(String adminLastName2) {
        this.adminLastName2 = adminLastName2;
    }

    public Boolean getCurrentUserIsMember() {
        return currentUserIsMember;
    }

    public void setCurrentUserIsMember(Boolean currentUserIsMember) {
        this.currentUserIsMember = currentUserIsMember;
    }

    public Long getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Long membersCount) {
        this.membersCount = membersCount;
    }

    public Long getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(Long messagesCount) {
        this.messagesCount = messagesCount;
    }
}
