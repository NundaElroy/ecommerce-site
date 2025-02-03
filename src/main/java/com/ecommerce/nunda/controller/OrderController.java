package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.service.CartService;
import com.ecommerce.nunda.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;


@Controller
@SessionAttributes("billingDetails")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;




    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/orders")
    public String getOrder(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "order/order";
    }



    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/process-order")
    public RedirectView processUserOrder(@Valid BillingDetailsDTO billingDetailsDTO,
                                         BindingResult bindingResult,
                                         Principal principal,
                                         RedirectAttributes redirectAttributes,
                                         HttpSession session) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid billing details!");
            return new RedirectView("/checkout");
        }

        // Process the order
        Orders order = orderService.createOrder( principal.getName(),billingDetailsDTO);

        //clear Cart
        cartService.processCartAfterOrderProcess(principal.getName());


        // Store the paymentRequest object in session
        session.setAttribute("order", order);
        session.setAttribute("billingDetails", billingDetailsDTO );

        // Redirect to payment processing controller
        return new RedirectView("/initiate-payment");
    }




}
