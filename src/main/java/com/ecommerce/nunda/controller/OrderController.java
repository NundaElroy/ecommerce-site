package com.ecommerce.nunda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/admin/orders")
    public String getOrder(){
        return "order/order";
    }
}
