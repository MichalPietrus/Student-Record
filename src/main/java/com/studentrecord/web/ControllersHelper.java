package com.studentrecord.web;

import com.studentrecord.model.*;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ControllersHelper {

    /**
     * Methods for admin and student controller.
     */

    static void setUserDetails(User user, UserDetailsDB userDetails, Parent parent, PlaceOfResident placeOfResident, User userDB) {
        int userDetailsDbId = 0;
        int userDBParentId = 0;
        int userDBPlaceOfResidentId = 0;
        if (userDB.getUserDetailsDB() != null) {
            userDetailsDbId = userDB.getUserDetailsDB().getId();
            userDBParentId = userDB.getUserDetailsDB().getParent().getId();
            userDBPlaceOfResidentId = userDB.getUserDetailsDB().getPlaceOfResident().getId();
        }
        userDB.setFirstName(user.getFirstName());
        userDB.setLastName(user.getLastName());
        userDB.setEmail(user.getEmail());
        userDB.setPESEL(user.getPESEL());
        userDB.setUserDetailsDB(userDetails);
        userDB.getUserDetailsDB().setParent(parent);
        userDB.getUserDetailsDB().setPlaceOfResident(placeOfResident);
        if (userDetailsDbId != 0 && userDBParentId != 0 && userDBPlaceOfResidentId != 0) {
            userDB.getUserDetailsDB().setId(userDetailsDbId);
            userDB.getUserDetailsDB().getParent().setId(userDBParentId);
            userDB.getUserDetailsDB().getPlaceOfResident().setId(userDBPlaceOfResidentId);
        }
    }

    static void extractTeachers(List<User> users, List<User> teachers) {
        for (User user : users) {
            Optional<Role> role = user.getRoles().stream()
                    .filter(u -> u.getName().equals("ROLE_TEACHER"))
                    .findAny();
            if (role.isPresent())
                teachers.add(user);
        }
    }

    static void setUserDetailsAndModels(Model model, User user) {
        UserDetailsDB userDetailsDB;
        PlaceOfResident placeOfResident;
        Parent parent;
        if (user.getUserDetailsDB() == null) {
            userDetailsDB = new UserDetailsDB();
            placeOfResident = new PlaceOfResident();
            parent = new Parent();
        } else {
            userDetailsDB = user.getUserDetailsDB();
            placeOfResident = user.getUserDetailsDB().getPlaceOfResident();
            parent = user.getUserDetailsDB().getParent();
        }
        model.addAttribute("userDetails", userDetailsDB);
        model.addAttribute("placeOfResident", placeOfResident);
        model.addAttribute("parent", parent);
        model.addAttribute("user", user);
    }

    /**
     * Methods for teacher controller.
     */

    static void setGradeDetails(String subjectName, Integer semester, String category,
                                Integer rating, Integer ratingWeight, User user, Grade grade) {
        grade.setCategory(category);
        grade.setRating(rating);
        grade.setRatingWeight(ratingWeight);
        setSubjectForGrade(subjectName, user, grade);
        grade.setTimestamp(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()));
        grade.setSemester(semester);
    }

    static void setSubjectForGrade(String subjectName, User user, Grade grade) {
        List<Subject> subjects = user.getSchoolClass().getSubjects();
        for (int i = 0; i < subjects.size(); i++)
            if (subjects.get(i).getName().equals(subjectName))
                grade.setSubject(subjects.get(i));
    }

    static void setTeacherDetails(User user, UserDetailsDB userDetails, PlaceOfResident placeOfResident, User userDB) {
        int userDetailsDbId = 0;
        int userDBPlaceOfResidentId = 0;
        if (userDB.getUserDetailsDB() != null) {
            userDetailsDbId = userDB.getUserDetailsDB().getId();
            userDBPlaceOfResidentId = userDB.getUserDetailsDB().getPlaceOfResident().getId();
        }
        userDB.setFirstName(user.getFirstName());
        userDB.setLastName(user.getLastName());
        userDB.setEmail(user.getEmail());
        userDB.setPESEL(user.getPESEL());
        userDB.setUserDetailsDB(userDetails);
        userDB.getUserDetailsDB().setPlaceOfResident(placeOfResident);
        if (userDetailsDbId != 0 && userDBPlaceOfResidentId != 0) {
            userDB.getUserDetailsDB().setId(userDetailsDbId);
            userDB.getUserDetailsDB().getPlaceOfResident().setId(userDBPlaceOfResidentId);
        }
    }

}
