package com.studentrecord.service;

import com.studentrecord.model.UserDetailsDB;
import com.studentrecord.web.dto.UserRegistrationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.studentrecord.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User register(UserRegistrationDto registration);

    User saveAndEncode(User user);

    User saveWithoutEncoding(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    List<User> findByKeyword(String keyword);

    void delete(User user);

    List<User> findAllStudents();

    List<User> findStudentsByKeyword(String keyword);

    List<User> findAllPageable(Pageable pageable);

    List<User> findAllStudentsPageable(Pageable pageable);

    List<User> findByKeywordPageable(String keyword, Pageable pageable);

    List<User> findAllStudentsByKeywordPageable(String keyword, Pageable pageable);

}
