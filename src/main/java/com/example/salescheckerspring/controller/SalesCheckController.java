package com.example.salescheckerspring.controller;


import com.example.salescheckerspring.form.ProductSerachForm;
import com.example.salescheckerspring.model.Product;
import com.example.salescheckerspring.repository.ProductRepository;
import com.example.salescheckerspring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Controller
public class SalesCheckController {

    ProductRepository productRepository;

    @Autowired
    public SalesCheckController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    ProductService productService;


    public SalesCheckController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping(value = {"/index","/"})
    public String createEntries(Model model) {
        productRepository.saveAll(productService.getProducts());
        return "index";
    }


    @GetMapping(value = "/worstsellingproduct")
    public String worstSellPage(Model model){
        model.addAttribute("product" , productService.worstSellingProduct(productService.getProducts()));
        return "worstsellingproduct";
    }

    @GetMapping(value = "/bestsoldproduct")
    public String bestSoldProduct(Model model){
        model.addAttribute("product" , productService.bestSellingProduct(productService.getProducts()));
        return "bestsoldproduct";
    }

    @GetMapping(value = "/income")
    public String cashflow(Model model){
        model.addAttribute("product" , productService.totalCashFlow(productService.getProducts()));
        return "income";
    }

    @GetMapping(value = "/soldproductsnumber")
    public String soldProductsNumber(Model model){
        model.addAttribute("product" , ProductService.totalSoldProduct(productService.getProducts()));
        return "soldproductsnumber";
    }

    @GetMapping("/productfind")
    public String searchQuestions(Model model) {
        List<Product> questions = productService.getProducts();
        model.addAttribute("form", new ProductSerachForm());
        model.addAttribute("questions", questions);

        return "productfind";
    }

    @PostMapping("/productfind")
    public String displaySearchResults(ProductSerachForm form, Model model) {
        List<Product> questions = productService.getByForm(form);
        model.addAttribute("form", form);
        model.addAttribute("questions", questions);

        return "productfind";
    }

    @GetMapping(value = "/suppliers")
    public String getSuppliers(Model model){
            List<String> suppliers = productService.getSuppliers(productService.getProducts());
        model.addAttribute("product" , suppliers);
        return "suppliers";
    }


    @GetMapping("/fil_val_asc")
    public String valasc(Model model) {
        List<Product> entries = productRepository.findByIdGreaterThanOrderByValueAsc(0L);
        model.addAttribute("result", entries);

        return "result";
    }
    @GetMapping("/fil_val_desc")
    public String valdesc(Model model) {
        List<Product> entries = productRepository.findByIdGreaterThanOrderByValueDesc(0L);
        model.addAttribute("result", entries);

        return "result";
    }
    @GetMapping("/fil_quan_asc")
    public String quanasc(Model model) {
        List<Product> entries = productRepository.findByIdGreaterThanOrderByQuantityAsc(0L);
        model.addAttribute("result", entries);

        return "result";
    }
    @GetMapping("/fil_quan_desc")
    public String quandesc(Model model) {
        List<Product> entries = productRepository.findByIdGreaterThanOrderByQuantityDesc(0L);
        model.addAttribute("result", entries);

        return "result";
    }




}

