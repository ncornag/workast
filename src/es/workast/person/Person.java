package es.workast.person;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.workast.group.Group;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Entity
@Table(name = "Person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------- Properties

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastName1")
    private String lastName1;

    @Column(name = "lastName2")
    private String lastName2;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Followers", joinColumns = @JoinColumn(name = "personId"), inverseJoinColumns = @JoinColumn(name = "followerId"))
    private Set<Person> followers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Followers", joinColumns = @JoinColumn(name = "followerId"), inverseJoinColumns = @JoinColumn(name = "personId"))
    private Set<Person> following;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    //    @OneToMany(mappedBy = "groupId", fetch = FetchType.LAZY)
    @Transient
    private Set<Group> groups;

    // ---------- Methods

    public String getDisplayName() {
        return getName() + " " + getLastName1() + (getLastName2() == null ? "" : " " + getLastName2());
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
