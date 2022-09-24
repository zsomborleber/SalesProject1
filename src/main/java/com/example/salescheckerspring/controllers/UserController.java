package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.Form.NewPasswordForm;
import com.example.salescheckerspring.configs.WebSecurityConfig;
import com.example.salescheckerspring.models.Roles;
import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.models.emailVerification.Utility;
import com.example.salescheckerspring.repos.UserRepository;
import com.example.salescheckerspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("userprofile")
    public String getuserprofile(Model model){

        User user = userService.getLoggedInUser();
        model.addAttribute("currentuser", user);

        return "userprofile";
    }
    @GetMapping("/changepassword")
    public String changepassword(Model model){
        model.addAttribute("form", new NewPasswordForm());
        return "changepassword";
    }
    @PostMapping("/changepassword")
    public String changepasswo(Model model, NewPasswordForm newPasswordForm){
        if(webSecurityConfig.passwordEncoder().matches(newPasswordForm.getCurrentpassword(), userService.getLoggedInUser().getPassword()) &&
                Objects.equals(newPasswordForm.getNewpassword1(), newPasswordForm.getNewpassword2())){
            userService.getLoggedInUser().setPassword(newPasswordForm.getNewpassword2());
            userService.saveUser(userService.getLoggedInUser());
            return "home";
        }
        else{
            model.addAttribute("loginError", true);
            return "redirect:/changepassword";
        }
    }
}
