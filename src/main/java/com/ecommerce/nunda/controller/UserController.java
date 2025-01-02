package com.ecommerce.nunda.controller;


import com.ecommerce.nunda.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final CategoryService categoryService;

    public UserController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //guest page for non auth users
    @GetMapping({"/guest","","/home"})
    public String index(Model model ) {

        return "user/guest_user" ;
    }

    @GetMapping("/customer/home")
    public String authenticatedUserHomePage() {
        return "user/guest_user" ;
    }


}
