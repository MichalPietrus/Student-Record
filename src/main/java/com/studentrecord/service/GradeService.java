package com.studentrecord.service;

import com.studentrecord.model.Grade;

import java.util.Optional;

public interface GradeService {

    Optional<Grade> findById(Integer id);

    void delete(Grade grade);

    void deleteById(Integer gradeId);

}
