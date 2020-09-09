package com.studentrecord.model;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private User teacher;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;
    @OneToMany(mappedBy = "subject")
    private List<Grade> grades;
    @OneToOne(mappedBy = "subject")
    private SchoolHours schoolHours;

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject(String name, User teacher, SchoolClass schoolClass) {
        this.name = name;
        this.teacher = teacher;
        this.schoolClass = schoolClass;
    }

    public SchoolHours getSchoolHours() {
        return schoolHours;
    }

    public void setSchoolHours(SchoolHours schoolHours) {
        this.schoolHours = schoolHours;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
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

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacher=" + teacher +
                ", schoolClass=" + schoolClass +
                '}';
    }

}
