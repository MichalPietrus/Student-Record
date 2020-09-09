package com.studentrecord.dao;

import com.studentrecord.model.SchoolClass;

import java.util.ArrayList;
import java.util.List;

public class SchoolClassDao {

    private static final List<SchoolClass> schoolClasses = new ArrayList<>();

    public static List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

}
