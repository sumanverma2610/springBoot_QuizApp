package com.Quiz.QuizApplication.controller;

import com.Quiz.QuizApplication.entity.User;
import com.Quiz.QuizApplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(User user) {

        user.setRole("STUDENT");   // Default role

        try {
            userService.saveUser(user);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Username or email already exists (unique constraint on User)
            return "redirect:/register?error=Username+or+email+already+in+use";
        }

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}