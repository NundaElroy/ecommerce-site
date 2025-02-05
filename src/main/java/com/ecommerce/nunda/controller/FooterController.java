package com.ecommerce.nunda.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooterController {


    @GetMapping("/about")
    public String about() {
        return "footer/aboutus";
    }

    @GetMapping("/contact")
    public String contact() {
        return "footer/contact";
    }

    @GetMapping("/return-policy")
    public String returnPolicy() {
        return "footer/order&returns";
    }

    @GetMapping("/privacy-policy")
    public String privacyPolicy() {
        return "footer/privacypolicy";
    }

    @GetMapping("/terms-and-conditions")
    public String terms() {
        return "footer/terms&conditions";
    }
}
