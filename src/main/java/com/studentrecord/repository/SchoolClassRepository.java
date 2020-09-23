package com.studentrecord.repository;

import com.studentrecord.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Integer> {

    SchoolClass findByName(String name);

}
