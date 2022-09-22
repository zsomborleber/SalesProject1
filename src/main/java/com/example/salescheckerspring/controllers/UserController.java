package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.Form.NewPasswordForm;
import com.example.salescheckerspring.configs.WebSecurityConfig;
import com.example.salescheckerspring.models.*;
import com.example.salescheckerspring.models.emailVerification.Utility;
import com.example.salescheckerspring.repos.OrderRepository;
import com.example.salescheckerspring.repos.ProductRepository;
import com.example.salescheckerspring.repos.ShoppingCartRepository;
import com.example.salescheckerspring.services.ProductService;
import com.example.salescheckerspring.services.ShoppingCartService;
import com.example.salescheckerspring.services.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    private final UserService userService;
    private WebSecurityConfig webSecurityConfig;

    private ProductService productService;

    private ProductRepository productRepository;

    private ShoppingCartRepository shoppingCartRepository;

    private OrderRepository orderRepository;

    private ShoppingCartService shoppingCartService;

    public UserController(UserService userService, WebSecurityConfig webSecurityConfig, ProductService productService, ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, OrderRepository orderRepository, ShoppingCartService shoppingCartService) {
        this.userService = userService;
        this.webSecurityConfig = webSecurityConfig;
        this.productService = productService;
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<User> users = userService.findAllUser();
        model.addAttribute("users",users);
        List<Product> questions = productService.getProducts();
        model.addAttribute("products", questions);

        return "home";
    }

    @PostMapping("/home/{EANCode}")
    public String showCreateForm(Model model, @PathVariable long EANCode, int quantity) {
        ShoppingCart shoppingCart = new ShoppingCart(productService.findProduct(EANCode).getId()
                ,productService.findProduct(EANCode).getArticleName(),quantity,productService.findProduct(EANCode).getPrice()*quantity);
        shoppingCartRepository.save(shoppingCart);

        return "redirect:/home";
    }

    @GetMapping(value = {"/login", "/bejelentkezes"})
    public String getLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            List<Product> products = new ArrayList<>();
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
        if(Objects.equals(userService.getLoggedInUser().getPassword(), newPasswordForm.getCurrentpassword()) &&
                Objects.equals(newPasswordForm.getNewpassword1(), newPasswordForm.getNewpassword2())){
            userService.getLoggedInUser().setPassword(newPasswordForm.getNewpassword2());
            return "home";
        }
        else{
            return "changepassword";
        }
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        List<ShoppingCart> products = (List<ShoppingCart>) shoppingCartRepository.findByOrderedIsFalse();
        model.addAttribute("products", products);
        Order order = new Order();
        return "cart";
    }

    @PostMapping("/cart")
    public String makeOrder(Order order){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //ShoppingCart shoppingCart = new ShoppingCart();
        order.setOrderDescription("teszt");
        order.setCartItems((List<ShoppingCart>) shoppingCartRepository.findByOrderedIsFalse());
        order.setCustomer(userService.getLoggedInUser());
        orderRepository.save(order);
        shoppingCartService.setTrueAfterOrdered();
        return "redirect:/home";
    }
}
