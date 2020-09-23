package com.studentrecord.web;

import com.studentrecord.model.SchoolClass;
import com.studentrecord.model.User;
import com.studentrecord.service.SchoolClassService;
import com.studentrecord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    @Autowired
    private UserService userService;

    @Autowired
    private SchoolClassService schoolClassService;

    @GetMapping("/lista-uczniow/{pageId}")
    public String showAddStudentForm(Model model, String keyword,
                                     @PathVariable Integer pageId) {
        Pageable pageable = PageRequest.of(pageId, 5, Sort.by(Sort.Order.asc("firstName")));
        if (keyword != null)
            model.addAttribute("students", userService.findStudentsByKeyword(keyword));
        else
            model.addAttribute("students", userService.findAllStudentsPageable(pageable));
        return "/moderator/students-list";
    }

    @GetMapping("/zmien-klase/{id}")
    public String showChangeClassForm(Model model, @PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        List<SchoolClass> schoolClasses = schoolClassService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("schoolClasses", schoolClasses);
        return "/moderator/change-class";
    }

    /**
     * @param id              - id of the student which class will be changed
     * @param schoolClassName - class name which was selected in change class form
     *                        This controller changes class of the student and also sets student's number.
     */

    @PostMapping("/zmien-klase/{id}")
    public String saveClass(@PathVariable Long id,
                            @RequestParam(value = "schoolClass.name") String schoolClassName) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        SchoolClass schoolClass = schoolClassService.findByName(schoolClassName);
        SchoolClass oldSchoolClass = user.getSchoolClass();
        user.setSchoolClass(schoolClass);
        schoolClass.getUsers().add(user);
        List<User> students = schoolClass.getUsers();
        students.sort(Comparator.comparing(User::getFirstName));
        for (int i = 0; i < students.size(); i++) {
            students.get(i).setStudentNumber(i + 1);
        }
        if (oldSchoolClass != null && !oldSchoolClass.getName().equals(schoolClassName)) {
            oldSchoolClass.getUsers().remove(user);
            List<User> oldClassStudents = oldSchoolClass.getUsers();
            oldClassStudents.sort(Comparator.comparing(User::getFirstName));
            for (int x = 0; x < oldClassStudents.size(); x++) {
                oldClassStudents.get(x).setStudentNumber(x + 1);
            }
        }
        userService.saveWithoutEncoding(user);
        return "redirect:/moderator/lista-uczniow/0";
    }

}
