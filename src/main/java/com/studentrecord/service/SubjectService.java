package com.studentrecord.service;

import com.studentrecord.model.SchoolClass;
import com.studentrecord.model.Subject;

public interface SubjectService {

    Subject save(Subject subject);

    Subject findByName(String name);

}
