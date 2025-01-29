package com.ecommerce.nunda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/account-management")
    public String accountManagement(Model model) {
        model.addAttribute("title", "Account Management");
        model.addAttribute("activeTab", "account-details");
        return "accountmanagement/accountmanagement";
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model) {
        model.addAttribute("title", "My Orders");
        model.addAttribute("activeTab", "orders");
        return "accountmanagement/myorders";
    }

    @GetMapping("/my-review")
    public String myReviews(Model model) {
        model.addAttribute("title", "My Reviews");
        model.addAttribute("activeTab", "reviews");
        return "accountmanagement/myreviews";
    }

    @GetMapping("/address-book")
    public String addressBook(Model model) {
        model.addAttribute("title", "Address Book");
        model.addAttribute("activeTab", "address");
        return "accountmanagement/address";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("title", "Change Password");
        model.addAttribute("activeTab", "password");
        return "accountmanagement/changepassword";
    }
}