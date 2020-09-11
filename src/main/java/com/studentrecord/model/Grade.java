package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
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

}
