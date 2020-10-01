package com.studentrecord.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
