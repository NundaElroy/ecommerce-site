package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.dto.AccountDetailsDto;
import com.ecommerce.nunda.dto.ChangePasswordDto;
import com.ecommerce.nunda.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AccountController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/account-management")
    public String accountManagement(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        AccountDetailsDto dto = new AccountDetailsDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhoneNumber());

        model.addAttribute("accountDetailsDto", dto);
        model.addAttribute("title", "Account Management");
        model.addAttribute("activeTab", "account-details");

        return "accountmanagement/accountmanagement";
    }


    @PostMapping("/account-management")
    public String accountManagementPost(@Valid @ModelAttribute AccountDetailsDto accountDetailsDto,
                                        BindingResult result,
                                        Model model,
                                        RedirectAttributes redirectAttributes,
                                        Principal principal) {

        if (result.hasErrors()) {
            return setupAccountManagementView(model);
        }


        String email = principal.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        if(userService.checkIfUserEmailAlreadyExists(accountDetailsDto.getEmail())){
            result.rejectValue("email", "error.accountDetailsDto", "Email already exists");
            return setupAccountManagementView(model);
        }


        // Update user details
        user.setFirstName(accountDetailsDto.getFirstName());
        user.setLastName(accountDetailsDto.getLastName());
        user.setPhoneNumber(accountDetailsDto.getPhone());
        user.setEmail(accountDetailsDto.getEmail());
        userService.saveUser(user);

        // Flash success message and redirect
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/account-management";
    }

    /**
     * Sets up the model attributes for the account management page.
     */
    private String setupAccountManagementView(Model model) {
        model.addAttribute("title", "Account Management");
        model.addAttribute("activeTab", "account-details");
        return "accountmanagement/accountmanagement";
    }



    @GetMapping("/my-orders")
    public String myOrders(Model model,Principal principal) {
        model.addAttribute("title", "My Orders");
        model.addAttribute("activeTab", "orders");

        String email = principal.getName();
        List<Orders> orders = userService.getAllCustomerOrders(email);

        model.addAttribute("orders", orders );
        return "accountmanagement/myorders";
    }

    @GetMapping("/my-review")
    public String myReviews(Model model,Principal principal) {
        List<Product> productReview  = userService.getAllProductsForReview(principal.getName());
        model.addAttribute("productReview", productReview);
        model.addAttribute("title", "My Reviews");
        model.addAttribute("activeTab", "reviews");
        return "accountmanagement/myreviews";
    }

    @GetMapping("/address-book")
    public String addressBook(Model model,Principal principal) {
        String email = principal.getName();
        List<Orders> orders = userService.getAllCustomerOrders(email);


        model.addAttribute("orders", orders );
        model.addAttribute("title", "Address Book");
        model.addAttribute("activeTab", "address");
        return "accountmanagement/address";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("title", "Change Password");
        model.addAttribute("activeTab", "password");


        // Ensure an empty DTO is passed to the view
        if (!model.containsAttribute("changePasswordDto")) {
            model.addAttribute("changePasswordDto", new ChangePasswordDto());
        }

        return "accountmanagement/changepassword";
    }

    @PostMapping("/change-password")
    public String changePasswordPost(@Valid @ModelAttribute ChangePasswordDto changePasswordDto,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes,
                                     Principal principal) {

        if (result.hasErrors()) {
            return handleValidationErrors(model);
        }

        if (!changePasswordDto.isPasswordMatching()) {
            result.rejectValue("confirmNewPassword", "error.changePasswordDto", "Passwords do not match");
            return handleValidationErrors(model);
        }


        String email = principal.getName();
        User user    = userService.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            result.rejectValue("currentPassword", "error.changePasswordDto", "Current password is incorrect");
            return handleValidationErrors(model);
        }


        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userService.saveUser(user);


        redirectAttributes.addFlashAttribute("success", "Password updated successfully!");
        return "redirect:/change-password";
    }

    /**
     * Handles validation errors by setting necessary attributes and returning the view.
     */
    private String handleValidationErrors(Model model) {
        model.addAttribute("title", "Change Password");
        model.addAttribute("activeTab", "password");
        return "accountmanagement/changepassword";
    }

}