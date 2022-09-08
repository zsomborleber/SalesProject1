package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.repos.ProductPastRepository;
import com.example.salescheckerspring.repos.ProductRepository;
import com.example.salescheckerspring.services.ProductPastService;
import com.example.salescheckerspring.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    private ProductPastService productPastService;

    public ProductController(ProductPastService productPastService) {
        this.productPastService = productPastService;
    }

    @GetMapping(value={"/","/index"})
    private String index(Model model){
        productPastService.saveProducts();
        return "index";
    }
}