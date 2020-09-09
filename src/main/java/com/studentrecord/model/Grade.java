package com.studentrecord.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int rating;
    private String timestamp;
    @Size(max = 100)
    private String comment;
    private String category;
    private int ratingWeight;
    private int semester;
    private String isFinal;
    @ManyToOne
    private Subject subject;
    @OneToOne(mappedBy = "grade")
    private User teacher;
    @ManyToOne
    private User student;
    @OneToOne
    @JoinColumn(name = "correction_grade_id", referencedColumnName = "id")
    private Grade correctionGrade;

    public Grade() {
    }

    public Grade(int rating, String timestamp, String category, String comment, int semester,
                 int ratingWeight, Subject subject, User teacher, Grade correctionGrade, User student, String isFinal) {
        this.isFinal = isFinal;
        this.semester = semester;
        this.rating = rating;
        this.timestamp = timestamp;
        this.category = category;
        this.comment = comment;
        this.ratingWeight = ratingWeight;
        this.subject = subject;
        this.teacher = teacher;
        this.correctionGrade = correctionGrade;
        this.student = student;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int grade) {
        this.rating = grade;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRatingWeight() {
        return ratingWeight;
    }

    public void setRatingWeight(int rating_weight) {
        this.ratingWeight = rating_weight;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User user) {
        this.teacher = user;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Grade getCorrectionGrade() {
        return correctionGrade;
    }

    public void setCorrectionGrade(Grade correctionGrade) {
        this.correctionGrade = correctionGrade;
    }

    public String getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(String isFinal) {
        this.isFinal = isFinal;
    }

    public String printRatingWeight() {
        String result;
        if (!this.category.equals("zw") && !this.category.equals("bz") && !this.category.equals("np"))
            result = Integer.toString(this.ratingWeight);
        else
            result = this.category;
        return result;
    }

    public String printRating() {
        String result;
        if (!this.category.equals("zw") && !this.category.equals("bz") && !this.category.equals("np"))
            result = Integer.toString(this.rating);
        else
            result = this.category;
        return result;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", grade=" + rating +
                ", Timestamp='" + timestamp + '\'' +
                ", comment='" + comment + '\'' +
                ", ratingWeight=" + ratingWeight +
                '}';
    }
}
