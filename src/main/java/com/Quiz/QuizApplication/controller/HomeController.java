package com.Quiz.QuizApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
    @GetMapping("/register")
    public String register(
            @RequestParam(required = false) String error,
            Model model) {
        model.addAttribute("error", error);
        return "register";
    }
}