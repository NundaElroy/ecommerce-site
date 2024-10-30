package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/orders")
    public String getOrder(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "order/order";
    }
}
