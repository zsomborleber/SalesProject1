package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.configs.WebSecurityConfig;
import com.example.salescheckerspring.models.Roles;
import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.models.emailVerification.Utility;
import com.example.salescheckerspring.services.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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
    public String processRegistration(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String siteUrl = Utility.getSiteUrl(request);

       if (!(userService.isEmailAlreadyInUse(user))) {
           user.setRole(Roles.USER);
            userService.saveUser(user);
            userService.sendVerificationEmail(user,siteUrl);

            return "register_success";
        }
       return "redirect:/login";

    }
    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code){
        if (userService.verify(code)){
            return "verify_success";
        }else {
            return "verify_fail";
        }


        /*boolean verified = userService.verify(code);
        String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed";
        model.addAttribute("pageTitle", pageTitle);

        return (verified ? "verify_success" : "verify_fail");*/
    }

}
