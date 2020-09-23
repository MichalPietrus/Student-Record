package com.studentrecord.service;

import com.studentrecord.model.Grade;
import com.studentrecord.model.User;

import java.util.Optional;

public interface GradeService {

    Optional<Grade> findById(Integer id);

    void delete(Grade grade);

    void deleteById(Integer gradeId);

    void setGradeDetails(String subjectName, Integer semester, String category,
                         Integer rating, Integer ratingWeight, User user, Grade grade);

    void setSubjectForGrade(String subjectName, User user, Grade grade);

}
