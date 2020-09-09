package com.studentrecord.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String parentFirstName;
    private String parentLastName;
    @Email
    private String parentEmail;
    @Pattern(regexp = "^(?:[+][0-9]{2}\\s?[0-9]{3}[-]?[0-9]{3,}|(?:[(][0-9]{3}[)]|[0-9]{3})\\s*[-]?\\s*[0-9]{3}[-][0-9]{4})(?:\\s*x\\s*[0-9]+)?")
    private String parentPhoneNumber;
    @OneToOne(mappedBy = "parent")
    private UserDetailsDB userDetailsDB;

    public Parent() {
    }

    public Parent(String parentFirstName, String parentLastName, String parentEmail, String parentPhoneNumber, UserDetailsDB userDetailsDB) {
        this.parentFirstName = parentFirstName;
        this.parentLastName = parentLastName;
        this.parentEmail = parentEmail;
        this.parentPhoneNumber = parentPhoneNumber;
        this.userDetailsDB = userDetailsDB;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParentFirstName() {
        return parentFirstName;
    }

    public void setParentFirstName(String firstName) {
        this.parentFirstName = firstName;
    }

    public String getParentLastName() {
        return parentLastName;
    }

    public void setParentLastName(String lastName) {
        this.parentLastName = lastName;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String email) {
        this.parentEmail = email;
    }

    public String getParentPhoneNumber() {
        return parentPhoneNumber;
    }

    public void setParentPhoneNumber(String phoneNumber) {
        this.parentPhoneNumber = phoneNumber;
    }

    public UserDetailsDB getUserDetailsDB() {
        return userDetailsDB;
    }

    public void setUserDetailsDB(UserDetailsDB userDetailsDB) {
        this.userDetailsDB = userDetailsDB;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", firstName='" + parentFirstName + '\'' +
                ", lastName='" + parentLastName + '\'' +
                ", email='" + parentEmail + '\'' +
                ", phoneNumber='" + parentPhoneNumber + '\'' +
                '}';
    }
}
