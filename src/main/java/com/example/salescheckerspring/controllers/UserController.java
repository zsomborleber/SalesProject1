package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.configs.WebSecurityConfig;
import com.example.salescheckerspring.models.Roles;
import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @GetMapping(value = {"/login", "/bejelentkezes"})
    public String getLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {

            return "login";
        }
        return "loggedin";
    }


    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process-register")
    public String processRegistration(User user) {
        if (!(userService.isEmailAlreadyInUse(user))) {
            user.setRole(Roles.USER);
            userService.saveUser(user);

            return "redirect:/login";
        }
        return "/signup_form";
    }
}
