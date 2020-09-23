package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
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

    public String getSchoolClassNameAndYear() {
        if (this.year != null && this.name != null)
            return this.name + " " + "(" + this.year + ")";
        else
            return this.name;
    }

}