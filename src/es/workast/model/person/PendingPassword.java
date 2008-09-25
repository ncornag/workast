package es.workast.model.person;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 *
 * @author gianu
 */
@Entity
@Table(name="PendingPassword")
public class PendingPassword implements Serializable {

    private static final long serialVersionUID = 1L;

    public PendingPassword() {
        
    }

    public PendingPassword(Person person, String uid) {
        this.person = person;
        this.uid = uid;
        this.insertedDate = Calendar.getInstance().getTime();
    }

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PendingPasswordSequence")
    @SequenceGenerator(name = "PendingPasswordSequence", sequenceName = "PendingPasswordSequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name="personId")
    private Person person;

    @Column(name="uid")
    private String uid;

    @Column(name="insertedDate")
    private Date insertedDate;


    // -- Accessors
    
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getInsertedDate() {
        return insertedDate;
    }

    public void setInsertedDate(Date insertedDate) {
        this.insertedDate = insertedDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}

        if (o == null || ! getClass().equals(o.getClass())) {return false;}

        final PendingPassword pendingPassword = (PendingPassword) o;

        if (pendingPassword.getId().equals(this.getId())) {return true;}
        
        return false;
    }
}
