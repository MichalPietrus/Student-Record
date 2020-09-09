package com.studentrecord.model;

import javax.persistence.*;

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

    public SchoolHours() {
    }

    public SchoolHours(String date, Subject subject, SchoolClass schoolClass) {
        this.date = date;
        this.subject = subject;
        this.schoolClass = schoolClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    @Override
    public String toString() {
        return "SchoolHours{" +
                "id=" + id +
                ", date='" + date + '\'' +
                '}';
    }
}
