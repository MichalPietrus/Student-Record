package com.studentrecord.service;

import com.studentrecord.model.Subject;
import com.studentrecord.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject findByName(String name) {
        return subjectRepository.findByName(name);
    }

}
