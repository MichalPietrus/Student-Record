package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
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
    @ToString.Exclude
    private SchoolClass schoolClass;
    @OneToMany(mappedBy = "subject")
    private List<Grade> grades;
    @OneToOne(mappedBy = "subject")
    private SchoolHours schoolHours;

    public Subject(String name) {
        this.name = name;
    }

    public Subject(String name, User teacher, SchoolClass schoolClass) {
        this.name = name;
        this.teacher = teacher;
        this.schoolClass = schoolClass;
    }
}
