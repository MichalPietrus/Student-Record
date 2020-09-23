package com.studentrecord.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @ToString.Exclude
    private Subject subject;
    @OneToOne(mappedBy = "grade")
    private User teacher;
    @ManyToOne
    @ToString.Exclude
    private User student;
    @OneToOne
    @JoinColumn(name = "correction_grade_id", referencedColumnName = "id")
    private Grade correctionGrade;

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
