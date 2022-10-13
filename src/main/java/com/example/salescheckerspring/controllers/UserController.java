package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.Form.NewPasswordForm;
import com.example.salescheckerspring.configs.WebSecurityConfig;
import com.example.salescheckerspring.models.*;
import com.example.salescheckerspring.models.emailVerification.Utility;
import com.example.salescheckerspring.repos.OrderRepository;
import com.example.salescheckerspring.repos.ProductRepository;
import com.example.salescheckerspring.repos.ShoppingCartRepository;
import com.example.salescheckerspring.repos.UserRepository;
import com.example.salescheckerspring.services.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private ProductPastService productPastService;

    private OrderService orderService;

    public UserController(UserService userService, WebSecurityConfig webSecurityConfig, ProductService productService, ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, OrderRepository orderRepository, ShoppingCartService shoppingCartService, ProductPastService productPastService, OrderService orderService) {
        this.userService = userService;
        this.webSecurityConfig = webSecurityConfig;
        this.productService = productService;
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
        this.productPastService = productPastService;
        this.orderService = orderService;
    }

    @GetMapping(value = {"/", "/index"})
    private String index() {
        productPastService.saveProducts();
        productService.loadProducts();
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model,
                       @Param("keyword") String keyword) {
        List<User> users = userService.findAllUser();
        model.addAttribute("users", users);
        List<Product> questions = productService.listAll(keyword);
        model.addAttribute("products", questions);
        model.addAttribute("keyword", keyword);
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
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getFieldError());
            return "signup_form";
        }
        String siteUrl = Utility.getSiteUrl(request);
        if (!(userService.isEmailAlreadyInUse(user)) && !userService.isTAXNumberAlreadyInUse(user)) {
            user.setRole(Roles.USER);
            userService.saveUserReg(user);
            userService.sendVerificationEmail(user, siteUrl);
            return "register_success";
        } else if (userService.isEmailAlreadyInUse(user)) {
            bindingResult.rejectValue("email", "error.email", "Ez az email cím már használtaban van");
            return "signup_form";
        } else if (userService.isTAXNumberAlreadyInUse(user)) {
            bindingResult.rejectValue("taxNumber", "error.taxNumber", "Ez az adószám már használtaban van");
            return "signup_form";
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
    }

    @GetMapping("userprofile")
    public String getuserprofile(Model model) {
        String email = userService.getLoggedInUser().getEmail();
        User user = userService.findUserByEmail(email).orElseThrow();
        model.addAttribute("currentuser", user);
        return "userprofile";
    }

    @GetMapping("/changepassword")
    public String changepassword(Model model) {
        model.addAttribute("form", new NewPasswordForm());
        return "changepassword";
    }

    @PostMapping("/changepassword")
    public String changepasswo(Model model, NewPasswordForm newPasswordForm) {
        if (webSecurityConfig.passwordEncoder().matches(newPasswordForm.getCurrentpassword(), userService.getLoggedInUser().getPassword()) &&
                Objects.equals(newPasswordForm.getNewpassword1(), newPasswordForm.getNewpassword2())) {
            userService.getLoggedInUser().setPassword(newPasswordForm.getNewpassword2());
            userService.saveUserReg(userService.getLoggedInUser());
            return "new_home";
        } else {
            model.addAttribute("loginError", true);
            return "redirect:/changepassword";
        }
    }

    @GetMapping(value = {"/connections"})
    public String connections(Model model) {
        return "connections";
    }

    @GetMapping("/cart")
    //TODO
    public String cart(Model model) {
        String email = userService.getLoggedInUser().getEmail();
        User user = userService.findUserByEmail(email).orElseThrow();
        List<ShoppingCart> products = (List<ShoppingCart>)
                shoppingCartRepository.findByOrderedIsFalseAndUserIsLike(userService.getLoggedInUser());
        model.addAttribute("amount", shoppingCartService.ShoppingCartSumOrderedAmount());
        model.addAttribute("amountwd", shoppingCartService.ShoppingCartSumOrderedAmount() *
                Math.abs((user.getDiscount() / 100) - 1));
        model.addAttribute("products", products);
        Order order = new Order();
        return "cart";
    }

    @PostMapping("/cart")
    public String makeOrder(Order order) throws MessagingException, UnsupportedEncodingException {
        order.setOrderDescription("teszt");
        order.setCartItems(shoppingCartRepository.findByOrderedIsFalseAndUserIsLike(userService.getLoggedInUser()));
        order.setCustomer(userService.getLoggedInUser());
        order.setLocalDate(LocalDate.now());
        orderRepository.save(order);
        shoppingCartService.setTrueAfterOrdered();
        userService.sendOrderVerificationEmail(userService.getLoggedInUser());
        return "redirect:/home";
    }

    @GetMapping("/orders")
    private String orders(Model model, @Param("keyword") String keyword) {
        List<Order> orders2 = orderService.listAll(keyword, userService.getLoggedInUser());
        model.addAttribute("orders", orders2);
        model.addAttribute("keyword", keyword);
        return "orders";
    }

}
