package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.services.ProductPastService;
import com.example.salescheckerspring.services.ProductService;
import com.example.salescheckerspring.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AppController {

    private ProductPastService productPastService;
    private ProductService productService;

    private UserService userService;

    public AppController(ProductPastService productPastService, ProductService productService, UserService userService) {
        this.productPastService = productPastService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping(value={"/","/index"})
    private String index(Model model){
        productPastService.saveProducts();
        productService.saveProducts();
        return "index";
    }

    @GetMapping(value={"/admin"})
    private String adminPage(Model model){
        return "admin";
    }

    @GetMapping("/admin/users")
    private String allUser(Model model){
        List<User> users = userService.findAllUser();
        model.addAttribute("users",users);

        return "admin_users";
    }
}