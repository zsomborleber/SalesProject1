package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.repos.ProductRepository;
import com.example.salescheckerspring.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value={"/","/index"})
    private String index(){
        return "index";
    }
}