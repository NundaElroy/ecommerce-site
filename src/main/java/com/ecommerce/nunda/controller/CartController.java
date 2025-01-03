package com.ecommerce.nunda.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    //when viewing cart
    @GetMapping("/cart")
    public String getUserCart(){
        return "user/cart";
    }

    @PostMapping("/addItemTocart/{id}")
    public String addItemToCart(@RequestParam("id")Long product_id){
        return "redirect:/cart";
    }
}
