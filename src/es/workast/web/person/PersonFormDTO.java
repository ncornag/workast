package es.workast.web.person;

import java.util.Date;

import javax.validation.constraints.Size;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class PersonFormDTO {

    // ---------- Properties

    @Size(min = 1, max = 50)
    private String name;

    @Size(min = 1, max = 50)
    private String lastName1;

    @Size(max = 50)
    private String lastName2;

    @Size(max = 50)
    private String title;

    @Size(max = 50)
    private String area;

    @Size(max = 50)
    private String city;

    private Date birthday;

    @Size(min = 1, max = 50)
    private String email;

    private String currentPassword;

    @Size(min = 5, max = 50, groups = PasswordGroup.class)
    private String password;

    private String passwordAgain;

    private String pictureId;

    // ---------- Accessors

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
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

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

}
