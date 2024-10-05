package com.ecommerce.nunda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminContoller {

    @GetMapping("/admin/home")
    public String adminPage() {
        return "index";
    }
}
