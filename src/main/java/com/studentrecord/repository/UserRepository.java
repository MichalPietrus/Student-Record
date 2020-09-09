package com.studentrecord.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.studentrecord.model.User;


import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "from User u where u.firstName like %:keyword% or u.lastName like %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "from User u where u.firstName like %:keyword% or u.lastName like %:keyword%")
    List<User> findByKeywordPageable(@Param("keyword") String keyword, Pageable pageable);

}
