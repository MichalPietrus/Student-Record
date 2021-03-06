package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// TODO: 29.09.2020 this class is for later feature

@Data
@NoArgsConstructor
@Entity
public class SchoolHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String date;

    @OneToOne
    private Subject subject;

    @ManyToOne
    private SchoolClass schoolClass;

    public SchoolHours(String date, Subject subject, SchoolClass schoolClass) {
        this.date = date;
        this.subject = subject;
        this.schoolClass = schoolClass;
    }
}
