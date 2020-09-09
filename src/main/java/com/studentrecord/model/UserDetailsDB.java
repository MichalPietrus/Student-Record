package com.studentrecord.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
public class UserDetailsDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Pattern(regexp = "^(?:[+][0-9]{2}\\s?[0-9]{3}[-]?[0-9]{3,}|(?:[(][0-9]{3}[)]|[0-9]{3})\\s*[-]?\\s*[0-9]{3}[-][0-9]{4})(?:\\s*x\\s*[0-9]+)?")
    private String phoneNumber;
    @Pattern(regexp = "^(0[1-9]|1[012])[-/.](0[1-9]|[12][0-9]|3[01])[-/.](19|20)\\d\\d$")
    private String birthday;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_of_resident_id")
    private PlaceOfResident placeOfResident;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Parent parent;
    @OneToOne(mappedBy = "userDetailsDB")
    private User user;

    public UserDetailsDB() {
    }

    public UserDetailsDB(String phoneNumber, String birthday, PlaceOfResident placeOfResident, Parent parent, User user) {
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.placeOfResident = placeOfResident;
        this.parent = parent;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public PlaceOfResident getPlaceOfResident() {
        return placeOfResident;
    }

    public void setPlaceOfResident(PlaceOfResident placeOfResident) {
        this.placeOfResident = placeOfResident;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserDetailsDB{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday=" + birthday +
                ", placeOfResident=" + placeOfResident +
                ", parent=" + parent +
                '}';
    }

}
