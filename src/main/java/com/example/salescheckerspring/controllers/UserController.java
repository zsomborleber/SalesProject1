package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.Configs.WebSecurityConfig;
import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    private UserService userService;
    private WebSecurityConfig webSecurityConfig;

    public UserController(UserService userService, WebSecurityConfig webSecurityConfig) {
        this.userService = userService;
        this.webSecurityConfig = webSecurityConfig;
    }
    @GetMapping("/home")
    public String home(Model model) {
        List<User> users = userService.findAllUser();
        model.addAttribute("users",users);
        return "home";
    }


    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process-register")
    public String processRegistration(User user) {
        if (!(userService.isEmailAlreadyInUse(user))) {
            userService.saveUser(user);

            return "redirect:/login";
        }
        return "/signup_form";
    }
}
