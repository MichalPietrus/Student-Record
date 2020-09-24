package com.studentrecord.web;

import com.studentrecord.model.*;
import com.studentrecord.service.SchoolClassService;
import com.studentrecord.service.SubjectService;
import com.studentrecord.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final SchoolClassService schoolClassService;

    private final SubjectService subjectService;

    private final UserService userService;

    public AdminController(SchoolClassService schoolClassService, SubjectService subjectService, UserService userService) {
        this.schoolClassService = schoolClassService;
        this.subjectService = subjectService;
        this.userService = userService;
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
        return "redirect:/";
    }

    @GetMapping("/dodaj-przedmiot")
    public String addSubjectForm(Model model) {
        List<SchoolClass> schoolClasses = schoolClassService.findAll();
        if (!schoolClasses.isEmpty())
            model.addAttribute("schoolClasses", schoolClasses);
        List<User> teachers = userService.findAllByRoleName("ROLE_TEACHER");
        model.addAttribute("subject", new Subject());
        model.addAttribute("teachers", teachers);
        return "admin/add-subject";
    }

    @PostMapping("/zapisz-przedmiot")
    public String saveSubject(@ModelAttribute("subject") Subject subject, @RequestParam(value = "schoolClass.id") Integer id,
                              @RequestParam(value = "teacher.id") Long teacherId) {
        Optional<SchoolClass> classOptional = schoolClassService.findById(id);
        classOptional.ifPresent(subject::setSchoolClass);
        Optional<User> userOptional = userService.findById(teacherId);
        userOptional.ifPresent(subject::setTeacher);
        subjectService.save(subject);
        return "redirect:/";
    }

    @GetMapping("/lista-uzytkownikow/{pageId}")
    public String changeRoleForm(Model model, String keyword,
                                 @PathVariable Integer pageId) {
        Pageable pageable = PageRequest.of(pageId, 5, Sort.by(Sort.Order.asc("firstName")));
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
        String role = user.getRoles().stream().findAny().get().getName();
        userService.setUserDetailsAndModels(model, user);
        model.addAttribute("role",role);
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
        userService.setUserDetails(user, userDetails, parent, placeOfResident, userDB);


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
        Role currentlySelectedRole = user.getRoles().stream().findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id: " + id));
        List<Role> roles = Arrays.asList(new Role("STUDENT"), new Role("TEACHER"),
                new Role("ADMIN"), new Role("MODERATOR"));
        model.addAttribute("currentlySelectedRole", currentlySelectedRole);
        model.addAttribute("roles", roles.stream()
                .filter(role -> !role.getName().equals(user.getRoleNameWithoutPrefix())).collect(Collectors.toList()));
        model.addAttribute("user", user);
        return "admin/change-role";
    }

    @PostMapping("zapisz-role/{id}")
    public String saveRole(@PathVariable Long id,
                           @RequestParam(value = "role.name") String role) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        String changedRole;
        if (!role.contains("ROLE_")) {
            changedRole = "ROLE_" + role;
            user.getRoles().forEach(role1 -> role1.setName(changedRole));
        }
        userService.saveWithoutEncoding(user);
        return "redirect:/admin/lista-uzytkownikow/0";
    }

    @GetMapping("usun-uzytkownika/{id}/{pageId}")
    public String deleteUser(@PathVariable Long id,
                             @PathVariable("pageId") Integer pageId, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        userService.delete(user);
        redirectAttributes.addAttribute("pageId", pageId);
        return "redirect:/admin/lista-uzytkownikow/{pageId}";
    }

}
