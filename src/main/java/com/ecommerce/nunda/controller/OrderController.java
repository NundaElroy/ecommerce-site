package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.service.CartService;
import com.ecommerce.nunda.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;

@Controller
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/orders")
    public String getOrder(Model model) {
        logger.info("Fetching all orders for admin view");
        model.addAttribute("orders", orderService.getAllOrders());
        return "order/order";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/process-order")
    public RedirectView processUserOrder(@Valid BillingDetailsDTO billingDetailsDTO,
                                         BindingResult bindingResult,
                                         Principal principal,
                                         RedirectAttributes redirectAttributes,
                                         HttpSession session) {

        logger.info("Processing order for user: {}", principal.getName());

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error ->
                    logger.warn("Validation error: {}", error.getDefaultMessage())
            );

            logger.warn("Billing details validation failed for user: {}",
                    principal != null ? principal.getName() : "Anonymous");

            redirectAttributes.addFlashAttribute("errorMessage", "Invalid billing details!");
            return new RedirectView("/checkout");
        }

        // Process the order
        Orders order = orderService.createOrder(principal.getName(), billingDetailsDTO);
        logger.info("Order created successfully for user: {} | Order ID: {}", principal.getName(), order.getOrder_id());

        // Clear cart
        cartService.processCartAfterOrderProcess(principal.getName());
        logger.info("Cart cleared after order processing for user: {}", principal.getName());

        // Store order details in session
        session.setAttribute("order", order);
        session.setAttribute("billingDetails", billingDetailsDTO);
        logger.info("Order details stored in session for user: {}", principal.getName());

        // Redirect to payment processing
        return new RedirectView("/initiate-payment");
    }
}
