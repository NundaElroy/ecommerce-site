package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.entity.Wishlist;
import com.ecommerce.nunda.dto.UserForm;
import com.ecommerce.nunda.service.CartService;
import com.ecommerce.nunda.service.EmailService;
import com.ecommerce.nunda.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    public AuthController(UserService userService, @Qualifier("javaMailService")EmailService emailService, PasswordEncoder passwordEncoder, CartService cartService) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
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
    public String registerUser(@Valid  @ModelAttribute("userForm") UserForm userForm,
                               BindingResult bindingResult,
                               @CookieValue(value = "usercart",required = false) String usercart,
                               HttpServletResponse resp,
                               HttpServletRequest req) throws JsonProcessingException {

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
        user.setCart(new Cart());
        user.setWishlist(new Wishlist());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setRole("ROLE_USER");



         userService.registerUser(user);


        //send email for confirmation
        emailService.sendConfirmationEmail(user.getFirstName(), user.getEmail());

        if (usercart != null) {
            // Move the usercart cookie items to the user's cart
             cartService.moveCookieCartItemsToCartForRegisteringUser(usercart, user.getEmail());

            // Delete the usercart cookie
            Cookie cookie = new Cookie("usercart", ""); // Set value to an empty string
            cookie.setPath("/"); // Ensure the path matches the original cookie path
            cookie.setMaxAge(0); // Set max age to 0 to delete the cookie
            resp.addCookie(cookie); // Add the cookie to the response to delete it

        }

        return  "redirect:/login";


    }
}
