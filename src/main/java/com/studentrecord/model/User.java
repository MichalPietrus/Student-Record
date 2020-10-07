package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Email
    @NotNull
    private String email;
    private String password;
    private String PESEL;
    private int studentNumber;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
    @ManyToOne
    @JoinColumn(name = "class_id")
    @ToString.Exclude
    private SchoolClass schoolClass;
    @OneToOne(mappedBy = "teacher")
    private Subject subject;
    @OneToOne(cascade = CascadeType.ALL)
    private UserDetailsDB userDetailsDB;
    @OneToOne
    private Grade grade;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> studentGrades;

    public void addGrade(Grade grade) {
        if (grade != null && !grade.getCategory().equals("")) {
            studentGrades.add(grade);
            grade.setStudent(this);
        }
    }

    public String getRoleNameWithoutPrefix() {
        Role role = this.roles.stream().findAny().orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        String[] strings = role.getName().split("_");
        return strings[1];
    }

    public String getSchoolClassName() {
        String name = null;
        if (this.schoolClass != null)
            name = this.schoolClass.getName();
        if (name == null)
            return "Brak";
        else
            return name;
    }

    public List<Grade> getStudentGradesBySemesterAndSubject(int semester, String subjectName) {
        List<Grade> filtredStudentGrades = new ArrayList<>();
        for (Grade grade : studentGrades) {
            if (grade.getSemester() == semester && grade.getSubject().getName().equals(subjectName)
                    && grade.getIsFinal() == null)
                filtredStudentGrades.add(grade);
        }
        return filtredStudentGrades;
    }

    public String getAverageGrade(String subjectName, int semester) {
        List<Grade> filteredStudentGrades = new ArrayList<>();
        double x = 0;
        double y = 0;
        double result;
        for (Grade grade : this.studentGrades) {
            if (grade.getSemester() == semester
                    && grade.getSubject().getName().equals(subjectName)
                    && semester != 3 && grade.getIsFinal() == null
                    && !(grade.getCategory().equals("bz") || grade.getCategory().equals("np") || grade.getCategory().equals("zw")))
                filteredStudentGrades.add(grade);
            else if (semester == 3
                    && grade.getSubject().getName().equals(subjectName)
                    && grade.getIsFinal() == null
                    && !(grade.getCategory().equals("bz") || grade.getCategory().equals("np") || grade.getCategory().equals("zw")))
                filteredStudentGrades.add(grade);
        }
        for (Grade grade : filteredStudentGrades) {
            double rating = grade.getRating();
            double ratingWeight = grade.getRatingWeight();
            x += rating * ratingWeight;
            y += ratingWeight;
        }
        if (y != 0) {
            result = x / y;
            DecimalFormat numberFormat = new DecimalFormat("#.0");
            return numberFormat.format(result);
        } else
            return "0";
    }

    public Integer getExpectedFinalGradeId(int semester,String subjectName, String isFinal) {
        int expectedFinalGradeId = 0;
        for (Grade studentGrade : this.studentGrades) {
            if (studentGrade.getIsFinal() != null)
                if (studentGrade.getIsFinal().equals(isFinal) && studentGrade.getSemester() == semester && studentGrade.getSubject().getName().equals(subjectName))
                    expectedFinalGradeId = studentGrade.getId();
        }
        return expectedFinalGradeId;
    }

    public String printExpectedFinalGrade(int semester,String subjectName, String isFinal, boolean isUserPanel) {
        int expectedFinalGrade = 0;
        for (Grade studentGrade : this.studentGrades) {
            if (studentGrade.getIsFinal() != null)
                if (studentGrade.getIsFinal().equals(isFinal) && studentGrade.getSemester() == semester && studentGrade.getSubject().getName().equals(subjectName))
                    expectedFinalGrade = studentGrade.getRating();
        }
        String result;
        if (expectedFinalGrade == 0 && !isUserPanel)
            result = "+";
        else if (expectedFinalGrade == 0)
            result = " - ";
        else
            result = Integer.toString(expectedFinalGrade);
        return result;
    }

}
