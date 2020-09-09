package com.studentrecord.service;

import com.studentrecord.model.Grade;
import com.studentrecord.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public Optional<Grade> findById(Integer id) {
        return gradeRepository.findById(id);
    }

    @Override
    public void delete(Grade grade) {
        gradeRepository.delete(grade);
    }

    @Override
    public void deleteById(Integer gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid grade Id: " + gradeId));
        gradeRepository.delete(grade);
    }


}
