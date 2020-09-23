package com.studentrecord.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.studentrecord.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User register(User registration);

    User saveAndEncode(User user);

    User saveWithoutEncoding(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    List<User> findByKeyword(String keyword);

    void delete(User user);

    List<User> findAllStudents();

    List<User> findStudentsByKeyword(String keyword);

    Page<User> findAllPageable(Pageable pageable);

    Page<User> findAllStudentsPageable(Pageable pageable);

    Page<User> findByKeywordPageable(String keyword, Pageable pageable);

    Page<User> findAllStudentsByKeywordPageable(String keyword, Pageable pageable);

}
