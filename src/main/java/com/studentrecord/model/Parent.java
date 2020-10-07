package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;

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
    private String parentPhoneNumber;
    @OneToOne(mappedBy = "parent")
    @ToString.Exclude
    private UserDetailsDB userDetailsDB;

}
