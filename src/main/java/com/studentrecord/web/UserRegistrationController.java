package com.studentrecord.web;

import com.studentrecord.model.User;
import com.studentrecord.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User userRegistration() {
        return new User();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid User user,
                                      BindingResult result) {
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null)
            result.rejectValue("email", null, "There is already an account registered with that email");
        if (result.hasErrors())
            return "registration";
        userService.register(user);
        return "redirect:/registration?success";
    }

}
