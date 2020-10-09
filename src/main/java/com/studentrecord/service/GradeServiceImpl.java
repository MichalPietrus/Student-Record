package com.studentrecord.service;

import com.studentrecord.model.Grade;
import com.studentrecord.model.Subject;
import com.studentrecord.model.User;
import com.studentrecord.repository.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

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

    @Override
    public void setGradeDetails(String subjectName, User user, Grade grade, Integer semester) {
        grade.setSemester(semester);
        List<Subject> subjects = user.getSchoolClass().getSubjects();
        for (Subject subject : subjects)
            if (subject.getName().equals(subjectName))
                grade.setSubject(subject);
        user.addGrade(grade);
    }
}
