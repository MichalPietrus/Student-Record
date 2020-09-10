package com.studentrecord.web;

import com.studentrecord.model.*;
import com.studentrecord.service.SchoolClassService;
import com.studentrecord.service.SubjectService;
import com.studentrecord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SchoolClassService schoolClassService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @GetMapping("/panel")
    public String showAdminPanel() {
        return "admin/admin-panel";
    }

    @GetMapping("/utworz-klase")
    public String createSchoolClassForm(Model model) {
        model.addAttribute("schoolClass", new SchoolClass());
        return "admin/create-school-class";
    }

    @PostMapping("/zapisz-klase")
    public String saveClass(@Valid SchoolClass schoolClass, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/create-school-class";
        schoolClassService.save(schoolClass);
        return "redirect:/admin/panel";
    }

    @GetMapping("/dodaj-przedmiot")
    public String addSubjectForm(Model model) {
        model.addAttribute("subject", new Subject());
        List<SchoolClass> schoolClasses = schoolClassService.findAll();
        if (!schoolClasses.isEmpty())
            model.addAttribute("schoolClasses", schoolClasses);
        List<User> users = userService.findAll();
        List<User> teachers = new ArrayList<>();
        ControllersHelper.extractTeachers(users, teachers);
        model.addAttribute("teachers", teachers);
        return "admin/add-subject";
    }

    @PostMapping("/zapisz-przedmiot")
    public String saveSubject(Subject subject, @RequestParam(value = "schoolClass.id") Integer id,
                              @RequestParam(value = "teacher.id") Long teacherId) {
        Optional<SchoolClass> classOptional = schoolClassService.findById(id);
        classOptional.ifPresent(subject::setSchoolClass);
        Optional<User> userOptional = userService.findById(teacherId);
        userOptional.ifPresent(subject::setTeacher);
        subjectService.save(subject);
        return "redirect:/admin/panel";
    }

    @GetMapping("/lista-uzytkownikow/{pageId}")
    public String changeRoleForm(Model model, String keyword,
                                 @PathVariable Integer pageId) {
        Pageable pageable = PageRequest.of(pageId, 5, Sort.by(Sort.Order.asc("firstName")));
        List<User> users = userService.findAllPageable(pageable.next());
        ControllersHelper.addPageModels(model, users, pageId);
        if (keyword != null)
            model.addAttribute("users", userService.findByKeywordPageable(keyword, pageable));
        else
            model.addAttribute("users", userService.findAllPageable(pageable));
        return "admin/show-users";
    }

    @GetMapping("/szczegoly-uzytkownika/{id}")
    public String showUserDetails(@PathVariable("id") long id,
                                  Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        ControllersHelper.setUserDetailsAndModels(model, user);
        return "admin/user-details";
    }

    @PostMapping("/zapisz-szczegoly-uzytkownika/{id}")
    public String updateUserDetails(@PathVariable("id") long id,
                                    @Valid User user, BindingResult userResult,
                                    @ModelAttribute("userDetails") @Valid UserDetailsDB userDetails, BindingResult userDetailsResult,
                                    @ModelAttribute("parent") @Valid Parent parent, BindingResult parentResult,
                                    @ModelAttribute("placeOfResident") @Valid PlaceOfResident placeOfResident, BindingResult placeOfResidentResult) {
        User userDB = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        ControllersHelper.setUserDetails(user, userDetails, parent, placeOfResident, userDB);
        if (userResult.hasErrors() || userDetailsResult.hasErrors() || parentResult.hasErrors() || placeOfResidentResult.hasErrors()) {
            user.setId(id);
            return "admin/user-details";
        }
        if (user.getPassword().equals(""))
            userService.saveWithoutEncoding(userDB);
        else {
            userDB.setPassword(user.getPassword());
            userService.saveAndEncode(userDB);
        }
        return "redirect:/admin/lista-uzytkownikow/0";
    }

    @GetMapping("/zmien-role/{id}")
    public String showChangeRoleForm(Model model, @PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        Role role = user.getRoles().stream().findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        List<Role> roles = Arrays.asList(new Role("STUDENT"), new Role("TEACHER"),
                new Role("ADMIN"), new Role("MODERATOR"));
        model.addAttribute("role", role);
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        return "admin/change-role";
    }

    @PostMapping("zapisz-role/{id}")
    public String saveRole(@PathVariable Long id,
                           @RequestParam(value = "role.name") String role) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        if (role.equals(""))
            role = user.getRoleNameWithoutPrefix(user);
        String changedRole = "ROLE_" + role;
        user.getRoles().forEach(role1 -> role1.setName(changedRole));
        userService.saveWithoutEncoding(user);
        return "redirect:/admin/lista-uzytkownikow/0";
    }

    @GetMapping("usun-uzytkownika/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        System.out.println("Im in deleteUser Controller = " + id);
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        userService.delete(user);
        model.addAttribute("users", userService.findAll());
        return "admin/show-users";
    }

}
