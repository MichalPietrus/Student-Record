package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
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
    @Pattern(regexp = "^$|^(?:[+][0-9]{2}\\s?[0-9]{3}[-]?[0-9]{3,}|(?:[(][0-9]{3}[)]|[0-9]{3})\\s*[-]?\\s*[0-9]{3}[-][0-9]{4})(?:\\s*x\\s*[0-9]+)?")
    private String parentPhoneNumber;
    @OneToOne(mappedBy = "parent")
    @ToString.Exclude
    private UserDetailsDB userDetailsDB;

}
