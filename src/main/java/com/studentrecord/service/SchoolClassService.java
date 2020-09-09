package com.studentrecord.service;

import com.studentrecord.model.SchoolClass;
import com.studentrecord.model.User;
import com.studentrecord.web.dto.UserRegistrationDto;

import java.util.List;
import java.util.Optional;

public interface SchoolClassService {

    Optional<SchoolClass> findById(Integer id);

    List<SchoolClass> findAll();

    SchoolClass save(SchoolClass schoolClass);

    SchoolClass findByName(String name);

}
