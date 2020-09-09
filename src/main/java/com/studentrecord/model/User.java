package com.studentrecord.model;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.DecimalFormat;
import java.util.*;

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
    @PESEL
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
    private SchoolClass schoolClass;
    @OneToOne(mappedBy = "teacher")
    private Subject subject;
    @OneToOne(cascade = CascadeType.ALL)
    private UserDetailsDB userDetailsDB;
    @OneToOne
    private Grade grade;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> studentGrades;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, Collection<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String email, String password, Collection<Role> roles, SchoolClass schoolClass, Subject subject) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.schoolClass = schoolClass;
        this.subject = subject;
    }

    public User(String email, String password, Collection<Role> roles, SchoolClass schoolClass, Subject subject, UserDetailsDB userDetailsDB) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.schoolClass = schoolClass;
        this.subject = subject;
        this.userDetailsDB = userDetailsDB;
    }

    public User(String PESEL, String password, String email, String firstName, String lastName,
                Collection<Role> roles, SchoolClass schoolClass, Subject subject, UserDetailsDB userDetailsDB, Grade grade) {
        this.PESEL = PESEL;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.schoolClass = schoolClass;
        this.subject = subject;
        this.userDetailsDB = userDetailsDB;
        this.grade = grade;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(int studentNumber, String PESEL, String firstName, String lastName, String email, String password,
                Collection<Role> roles, SchoolClass schoolClass, Subject subject, UserDetailsDB userDetailsDB,
                Grade grade, List<Grade> studentGrades) {
        this.studentNumber = studentNumber;
        this.PESEL = PESEL;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.schoolClass = schoolClass;
        this.subject = subject;
        this.userDetailsDB = userDetailsDB;
        this.grade = grade;
        this.studentGrades = studentGrades;
    }

    public List<Grade> getStudentGrades() {
        return studentGrades;
    }

    public void setStudentGrades(List<Grade> studentGrade) {
        this.studentGrades = studentGrade;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public UserDetailsDB getUserDetailsDB() {
        return userDetailsDB;
    }

    public void setUserDetailsDB(UserDetailsDB userDetailsDB) {
        this.userDetailsDB = userDetailsDB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void addGrade(Grade grade) {
        if (grade != null && !grade.getCategory().equals("")) {
            studentGrades.add(grade);
            grade.setStudent(this);
        }
    }

    public String getRoleNameWithoutPrefix(User user) {
        Role role = user.roles.stream().findAny().orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
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
        for (int i = 0; i < studentGrades.size(); i++) {
            Grade grade = studentGrades.get(i);
            if (grade.getSemester() == semester && grade.getSubject().getName().equals(subjectName) && studentGrades.get(i).getIsFinal() == null)
                filtredStudentGrades.add(grade);
        }
        return filtredStudentGrades;
    }

    public String getAverageGrade(String subjectName, int semester) {
        List<Grade> filteredStudentGrades = new ArrayList<>();
        double x = 0;
        double y = 0;
        double result;
        for (int i = 0; i < this.studentGrades.size(); i++) {
            Grade grade = this.studentGrades.get(i);
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

        for (int i = 0; i < filteredStudentGrades.size(); i++) {
            Grade grade = filteredStudentGrades.get(i);
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

    public Integer getExpectedFinalGradeId(int semester, String isFinal) {
        int expectedFinalGradeId = 0;
        for (int i = 0; i < this.studentGrades.size(); i++) {
            if (this.studentGrades.get(i).getIsFinal() != null)
                if (this.studentGrades.get(i).getIsFinal().equals(isFinal) && this.studentGrades.get(i).getSemester() == semester)
                    expectedFinalGradeId = this.studentGrades.get(i).getId();
        }
        return expectedFinalGradeId;
    }

    public String printExpectedFinalGrade(int semester, String isFinal, boolean isUserPanel) {
        int expectedFinalGrade = 0;
        for (int i = 0; i < this.studentGrades.size(); i++) {
            if (this.studentGrades.get(i).getIsFinal() != null)
                if (this.studentGrades.get(i).getIsFinal().equals(isFinal) && this.studentGrades.get(i).getSemester() == semester)
                    expectedFinalGrade = this.studentGrades.get(i).getRating();
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + "*********" + '\'' +
                ", roles=" + roles +
                '}';
    }
}
