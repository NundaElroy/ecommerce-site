package com.ecommerce.nunda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {
    @GetMapping("/admin/payments")
    public String getPayment(){
        return "payment/payment";
    }
}
