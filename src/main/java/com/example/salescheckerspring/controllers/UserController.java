package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.DTO.UserDto;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        List<Product> questions = productRepository.findAll();
        model.addAttribute("products", questions);

        return "new_home";
    }

    @PostMapping("/home/{EANCode}")
    public String showCreateForm(Model model, @PathVariable long EANCode, int quantity) {
        ShoppingCart shoppingCart = new ShoppingCart(productService.findProduct(EANCode).getId()
                , productService.findProduct(EANCode).getArticleName(), quantity,
                productService.findProduct(EANCode).getPrice() * quantity,
                userService.getLoggedInUser());
        shoppingCartRepository.save(shoppingCart);
        return "redirect:/home";
    }

    @GetMapping("/admin/user/{email}")
    public String userProfileForAdminCheck(@PathVariable("email") String email, Model model) {
        Optional<User> user = userService.findUserByEmail(email);
        UserDto userDto = new UserDto();
        model.addAttribute("user",user.orElseThrow());
        return "admin_discount";
    }

    @PostMapping(value={"/admin/user/update"})
    public String updateDiscount(UserDto userDto){
        Optional<User> user = userService.findUserByEmail(userDto.getEmail());
        userService.updateUser(user.orElseThrow(),userDto);
        return "redirect:/admin/user/" + user.get().getEmail();
    }


   /* @PostMapping("/admin/user/discount")
    public String userProfileForAdminCheck(User user){
        user.setDiscount(25);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }*/


    //Post mappingbe a usert magát adom át csak

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
    public String processRegistration(
            @Valid
            User user,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request)
            throws MessagingException,
            UnsupportedEncodingException {
        if (bindingResult.hasErrors()){
            model.addAttribute("error",bindingResult.getFieldError());
            return "signup_form";
        }

        String siteUrl = Utility.getSiteUrl(request);
        if (!(userService.isEmailAlreadyInUse(user))) {
            user.setRole(Roles.USER);
            userService.saveUser(user);
            userService.sendVerificationEmail(user, siteUrl);
            return "register_success";
        }
        return "redirect:/login";

    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }


        /*boolean verified = userService.verify(code);
        String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed";
        model.addAttribute("pageTitle", pageTitle);

        return (verified ? "verify_success" : "verify_fail");*/
    }

    @GetMapping("userprofile")
    public String getuserprofile(Model model) {

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
            return "new_home";
        }
        else{
            model.addAttribute("loginError", true);
            return "redirect:/changepassword";
        }

    }
    @GetMapping(value = {"/connections"})
    public String connections(Model model) {


        return"connections";
    }

    @GetMapping("/cart")
    //TODO
    public String cart(Model model) {
        String email = userService.getLoggedInUser().getEmail();
        User user = userService.findUserByEmail(email).orElseThrow();
        List<ShoppingCart> products = (List<ShoppingCart>)
                shoppingCartRepository.findByOrderedIsFalseAndUserIsLike(userService.getLoggedInUser());
        model.addAttribute("amount",shoppingCartService.ShoppingCartSumOrderedAmount());
        model.addAttribute("amountwd",shoppingCartService.ShoppingCartSumOrderedAmount()*
                Math.abs((user.getDiscount()/100)-1));
        model.addAttribute("products", products);
        Order order = new Order();
        return "cart";
    }

    @PostMapping("/cart")
    public String makeOrder(Order order) throws MessagingException, UnsupportedEncodingException {
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        ShoppingCart shoppingCart = new ShoppingCart();*/
        order.setOrderDescription("teszt");
        order.setCartItems(shoppingCartRepository.findByOrderedIsFalseAndUserIsLike(userService.getLoggedInUser()));
        order.setCustomer(userService.getLoggedInUser());
        orderRepository.save(order);
        shoppingCartService.setTrueAfterOrdered();
        userService.sendOrderVerificationEmail(userService.getLoggedInUser());
        return "redirect:/home";
    }
}
