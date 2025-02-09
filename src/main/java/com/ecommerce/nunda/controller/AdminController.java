package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final UserService userService;
    private final ProductService productService;

    public AdminController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/admin/home")
    public String adminPage(Model model) {
        model.addAttribute("totalProducts",productService.getTotalNumberOfProducts());
        model.addAttribute("totalCustomers",userService.getAllCustomers());
        return "admin/dashboard";
    }
}
