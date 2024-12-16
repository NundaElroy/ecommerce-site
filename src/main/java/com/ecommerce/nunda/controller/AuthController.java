package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.formvalidators.UserForm;
import com.ecommerce.nunda.service.EmailService;
import com.ecommerce.nunda.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String getLogin(){
        return "user/login";

    }

    @GetMapping("/register")
    public String getRegister(Model model){

        model.addAttribute("userForm",new UserForm());
        return "user/register";

    }

    //registering the user
    @PostMapping("/register")
    public String registerUser(@Valid  @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult){

        //input validation
        if(bindingResult.hasErrors()){
            return "user/register";
        }

        // Check if password and confirmPassword match
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userForm", "Passwords do not match.");
            return "user/register"; // Return to the form with an error message
        }

        User user  = new User();
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setRole("ROLE_USER");



         userService.registerUser(user);


        //send email for confirmation
        emailService.sendConfirmationEmail(user.getFirstName(), user.getEmail());

        return  "redirect:/login";




    }
}
