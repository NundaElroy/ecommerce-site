package com.ecommerce.nunda.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping({"/guest",""})
    public String index() {
        return "user/guest_user" ;
    }

    @GetMapping("/customer/home")
    public String authenticatedUserHomePage() {
        return "user/guest_user" ;
    }
}
