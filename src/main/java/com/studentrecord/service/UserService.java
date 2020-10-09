package com.studentrecord.service;


import com.studentrecord.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    void register(User user);

    void setStudentNumbers(String schoolClassName, User user, SchoolClass oldSchoolClass);

    void setUserDetailsAndModels(Model model, User user);

    void setUserDetails(User user, UserDetailsDB userDetails, Parent parent, PlaceOfResident placeOfResident, User userDB);

    void setTeacherDetails(User user, UserDetailsDB userDetails, PlaceOfResident placeOfResident, User userDB);

    void saveAndEncode(User user);

    void saveWithoutEncoding(User user);

    Optional<User> findById(Long id);

    void delete(User user);

    List<User> findStudentsByKeyword(String keyword);

    Page<User> findAllPageable(Pageable pageable);

    Page<User> findAllStudentsPageable(Pageable pageable);

    Page<User> findByKeywordPageable(String keyword, Pageable pageable);

    Page<User> findAllByKeywordAndRolePageable(String keyword, String role, Pageable pageable);

    List<User> findAllByRoleName(String role);

}
