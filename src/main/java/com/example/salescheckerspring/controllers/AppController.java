package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.services.ProductPastService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    private ProductPastService productPastService;

    public AppController(ProductPastService productPastService) {
        this.productPastService = productPastService;
    }

    @GetMapping(value={"/","/index"})
    private String index(Model model){
        productPastService.saveProducts();
        return "index";
    }

    @GetMapping(value={"/admin"})
    private String adminPage(Model model){
        return "admin";
    }
}