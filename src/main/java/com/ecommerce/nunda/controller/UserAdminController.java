package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.customexceptions.PaymentException;
import com.ecommerce.nunda.repository.PaymentRepo;
import com.ecommerce.nunda.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserAdminController {

    private  final UserService userService;
    private final PaymentRepo paymentRepo;

    public UserAdminController(UserService userService, PaymentRepo paymentRepo){
        this.userService = userService;
        this.paymentRepo = paymentRepo;
    }

    @GetMapping("/admin/users")
    public String  getUser(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "user/userdetails";
    }

    @GetMapping("/admin/payments")
    public String  getPaymentInfo(Model model){
        model.addAttribute("payments", paymentRepo.findAll());
        return "payment/payment";
    }

    @GetMapping("/admin/viewpayment/{id}")
    public String  getPaymentDetail(Model model, @PathVariable Long id){
         model.addAttribute("payment",paymentRepo.findById(id).orElseThrow(() -> new PaymentException("PAYMENT DETAILS NOT FOUND") ));
         return "payment/paymentdetail";
    }







}
