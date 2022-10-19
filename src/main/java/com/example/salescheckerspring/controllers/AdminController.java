package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.models.*;
import com.example.salescheckerspring.repos.OrderRepository;
import com.example.salescheckerspring.repos.ProductPastRepository;
import com.example.salescheckerspring.services.OrderService;
import com.example.salescheckerspring.services.ProductPastService;
import com.example.salescheckerspring.services.ProductService;
import com.example.salescheckerspring.services.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    private final OrderService orderService;
    private final ProductPastService productPastService;
    private final ProductService productService;

    private final UserService userService;

    private final ProductPastRepository productPastRepository;

    private final OrderRepository orderRepository;

    public AdminController(ProductPastService productPastService, ProductService productService, UserService userService, ProductPastRepository productPastRepository, OrderRepository orderRepository, OrderService orderService) {
        this.productPastService = productPastService;
        this.productService = productService;
        this.userService = userService;
        this.productPastRepository = productPastRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping(value = {"/admin"})
    private String uncompletedOrders(Model model,@Param("keyword") String keyword) {

        List<Order> notCompletedOrders = orderService.notOrderedList(keyword);
        model.addAttribute("orders",notCompletedOrders);
        model.addAttribute("keyword",keyword);
        return "admin";
    }
    @GetMapping(value = {"/admin/completedOrders"})
    private String completedOrders(Model model,@Param("keyword") String keyword) {
        List<Order> completedOrders = orderService.listById(keyword);
        model.addAttribute("orders", completedOrders);
        model.addAttribute("keyword",keyword);
        return "admin_completedorders";
    }

    @PostMapping(value = {"/admin/completed"})
    public String updateDiscount(boolean isCompleted, long id) throws MessagingException, UnsupportedEncodingException {
        Order order = orderRepository.findById(id).orElseThrow();
        if (isCompleted) {
            order.setCompleted(true);
            orderRepository.save(order);
            try{
                userService.sendOrderCompletedEmail(order);
            }catch(Exception e){
                return "redirect:/admin/completedOrders";
            }

        } else {
            order.setCompleted(false);
            orderRepository.save(order);
        }
        return "redirect:/admin/completedOrders";
    }

    @GetMapping(value = {"/admin/{year}"})
    private String adminPage(@PathVariable(name = "year") Long year,
                             Model model) {
        model.addAttribute("income",
                productPastService.totalCashFlow(year));
        return "admin";
    }

    @GetMapping("/admin/users")
    private String allUser(Model model) {
        List<User> users = userService.findAllUser();
        model.addAttribute("users", users);

        return "admin_users";
    }

    @GetMapping("/admin/income")
    public String income() {
        return "admin_income";
    }


    @PostMapping("/search")
    public String income(Long year, Model model) {
        if (year == null){
            return "redirect:/admin/income";
        }
        model.addAttribute("income", productPastService.totalCashFlow(year));
        return "admin_income";
    }


    @GetMapping(value = {"/admin/upload"})
    public String saveNewSpaceShip(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin_upload";
    }

    @PostMapping("/admin/upload")
    public String addSpaceShip(Product product) {
        productService.saveProducts(product);
        return "redirect:/home";
    }

    @GetMapping("/admin/user/{email}")
    public String userProfileForAdminCheck(@PathVariable("email") String email, Model model) {
        Optional<User> user = userService.findUserByEmail(email);
        model.addAttribute("user", user.orElseThrow());
        return "admin_discount";
    }

    @PostMapping(value = {"/admin/user/update"})
    public String updateDiscount(String email, float discount) {
        User user = userService.findUserByEmail(email).orElseThrow();
        user.setDiscount(discount);
        userService.saveUser(user);
        return "redirect:/admin/user/" + user.getEmail();
    }
    @GetMapping("/admin/pastproducts")
    public String home(Model model,
                       @Param("keyword") String keyword) {
        List<ProductPast> questions = productPastService.listAll(keyword);
        model.addAttribute("products", questions);
        model.addAttribute("keyword", keyword);
        return "admin_pastproducts";
    }
}