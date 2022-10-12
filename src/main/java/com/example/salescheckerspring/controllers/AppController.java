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
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class AppController {
    private OrderService orderService;
    private ProductPastService productPastService;
    private ProductService productService;

    private UserService userService;

    private ProductPastRepository productPastRepository;

    private OrderRepository orderRepository;

    public AppController(ProductPastService productPastService, ProductService productService, UserService userService, ProductPastRepository productPastRepository, OrderRepository orderRepository,OrderService orderService) {
        this.productPastService = productPastService;
        this.productService = productService;
        this.userService = userService;
        this.productPastRepository = productPastRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping(value={"/","/index"})
    private String index(Model model){
        productPastService.saveProducts();
        productService.loadProducts();
        return "index";
    }

    @GetMapping(value={"/admin"})
    private String adminPage(Model model){
        List<Order> orders = (List<Order>) orderRepository.findAll();
        model.addAttribute("orders",orders);
        return "admin";
    }

    @PostMapping(value={"/admin/completed"})
    public String updateDiscount(boolean isCompleted, long id) throws MessagingException, UnsupportedEncodingException {
        Order order = orderRepository.findById(id).orElseThrow();
        if (isCompleted){
            order.setCompleted(true);
            orderRepository.save(order);
            userService.sendOrderCompletedEmail(order);
        }else{
            order.setCompleted(false);
            orderRepository.save(order);
        }
        return "redirect:/admin";
    }

    @GetMapping(value={"/admin/{year}"})
    private String adminPage(@PathVariable(name = "year") int year ,
                             Model model){
        model.addAttribute("income",
                productPastService.totalCashFlow(year));
        return "admin";
    }

    @GetMapping("/admin/users")
    private String allUser(Model model){
        List<User> users = userService.findAllUser();
        model.addAttribute("users",users);

        return "admin_users";
    }

    @GetMapping("/orders")
    private String orders(Model model, @Param("keyword") String keyword){
        List<Order> orders2 = orderService.listAll(keyword,userService.getLoggedInUser());
        model.addAttribute("orders",orders2);
        model.addAttribute("keyword",keyword);
        return "orders";
    }


    @GetMapping("/admin/income")
    public String add(Model model) {
        return "admin_income";
    }


    @PostMapping("/search")
    public String doSearchEmployee(int year, Model model) {
        model.addAttribute("income", productPastService.totalCashFlow(year));
        return "admin_income";
    }


    @GetMapping(value = {"/admin/upload"})
    public String saveNewSpaceShip(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin_upload";
    }

    @PostMapping("/admin/upload")
    public String addSpaceShip(Product product){
        productService.saveProducts(product);
        return "redirect:/home";
    }
}