package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
@NoArgsConstructor
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

    public Parent(String parentFirstName, String parentLastName, String parentEmail, String parentPhoneNumber, UserDetailsDB userDetailsDB) {
        this.parentFirstName = parentFirstName;
        this.parentLastName = parentLastName;
        this.parentEmail = parentEmail;
        this.parentPhoneNumber = parentPhoneNumber;
        this.userDetailsDB = userDetailsDB;
    }

}
