package com.studentrecord.web;

import com.studentrecord.model.*;
import com.studentrecord.service.GradeService;
import com.studentrecord.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/uczen")
public class StudentController {

    private final UserService userService;

    public StudentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/oceny")
    public String showUsersForm(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "student/grades-list";
    }

    @GetMapping("/szczegoly-uzytkownika")
    public String showUserDetailsForm(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        userService.setUserDetailsAndModels(model, user);
        return "student/user-details";
    }

    @PostMapping("/zapisz-szczegoly-uzytkownika")
    public String saveUserDetails(@ModelAttribute("user") @Valid User user, BindingResult userResult,
                                  @ModelAttribute("userDetails") @Valid UserDetailsDB userDetails, BindingResult userDetailsResult,
                                  @ModelAttribute("parent") @Valid Parent parent, BindingResult parentResult,
                                  @ModelAttribute("placeOfResident") @Valid PlaceOfResident placeOfResident, BindingResult placeOfResidentResult,
                                  Principal principal) {
        User userDb = userService.findByEmail(principal.getName());
        userService.setUserDetails(user, userDetails, parent, placeOfResident, userDb);
        if (userResult.hasErrors() || userDetailsResult.hasErrors() || parentResult.hasErrors() || placeOfResidentResult.hasErrors())
            return "student/user-details";
        if (user.getPassword().equals(""))
            userService.saveWithoutEncoding(userDb);
        else {
            userDb.setPassword(user.getPassword());
            userService.saveAndEncode(userDb);
        }
        return "redirect:/";
    }

}
