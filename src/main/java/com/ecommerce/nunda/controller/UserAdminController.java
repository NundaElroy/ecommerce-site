package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserAdminController {

    private  final UserService userService;

    public UserAdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String  getUser(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "user/userdetails";
    }



}
