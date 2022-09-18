package com.example.salescheckerspring.controllers;

import com.example.salescheckerspring.models.Product;
import com.example.salescheckerspring.models.ProductPast;
import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.repos.ProductPastRepository;
import com.example.salescheckerspring.services.ProductPastService;
import com.example.salescheckerspring.services.ProductService;
import com.example.salescheckerspring.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AppController {

    private ProductPastService productPastService;
    private ProductService productService;

    private UserService userService;

    private ProductPastRepository productPastRepository;

    public AppController(ProductPastService productPastService, ProductService productService, UserService userService, ProductPastRepository productPastRepository) {
        this.productPastService = productPastService;
        this.productService = productService;
        this.userService = userService;
        this.productPastRepository = productPastRepository;
    }

    @GetMapping(value={"/","/index"})
    private String index(Model model){
        productPastService.saveProducts();
        productService.loadProducts();
        return "index";
    }

    @GetMapping(value={"/admin"})
    private String adminPage(){
        return "admin";
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


    @GetMapping("/admin/income")
    public String add(Model model) {
        List<ProductPast> listemployee = productPastService.getProducts();
        model.addAttribute("product", new ProductPast());
        return "income";
    }


    @PostMapping("/search")
    public String doSearchEmployee(@ModelAttribute("employeeSearchFormData") ProductPast formData, Model model) {
        ProductPast emp = productPastService.get(formData.getId());
        model.addAttribute("product", emp);
        return "income";
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