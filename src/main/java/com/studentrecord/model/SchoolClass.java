package com.studentrecord.model;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class")
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", unique = true)
    @Size(min = 1, max = 3)
    @NotNull
    private String name;
    @NotNull
    @Size(min = 9, max = 9)
    @Pattern(regexp = "^[0-9-]*$")
    private String year;
    @NotNull
    private String specialization;
    @OneToMany(mappedBy = "schoolClass", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users;
    @OneToMany(mappedBy = "schoolClass")
    private List<Subject> subjects;
    @OneToMany(mappedBy = "schoolClass")
    private List<SchoolHours> schoolHours;

    public SchoolClass() {
    }

    public SchoolClass(String name, String year, String specialization) {
        this.name = name;
        this.year = year;
        this.specialization = specialization;
    }

    public SchoolClass(String name, String year, String specialization,
                       List<User> users, List<Subject> subjects, List<SchoolHours> schoolHours) {
        this.name = name;
        this.year = year;
        this.specialization = specialization;
        this.users = users;
        this.subjects = subjects;
        this.schoolHours = schoolHours;
    }

    public List<SchoolHours> getSchoolHours() {
        return schoolHours;
    }

    public void setSchoolHours(List<SchoolHours> schoolHours) {
        this.schoolHours = schoolHours;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getSchoolClassNameAndYear() {
        return this.name + " " + "(" + year + ")";
    }

    @Override
    public String toString() {
        return "SchoolClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", specialization='" + specialization + '\'' +
                ", users=" + users +
                '}';
    }

}