package com.studentrecord.repository;

import com.studentrecord.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Page<User> findAllByRolesNameEquals(String role, Pageable pageable);

    @Query(value = "from User u where u.firstName like %:keyword% or u.lastName like %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "from User u where u.firstName like %:keyword% or u.lastName like %:keyword%")
    Page<User> findByKeywordPageable(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select u from User u left join u.roles r where (u.firstName like %:keyword% or u.lastName like %:keyword%) and r.name = :role")
    Page<User> findAllByKeywordAndRolesNameEqualsPageable(@Param("keyword") String keyword,@Param("role") String role, Pageable pageable);

    @Query(value = "select u from User u left join u.roles r where r.name = :role")
    List<User> findAllByRolesNameEquals(@Param("role") String role);
}
