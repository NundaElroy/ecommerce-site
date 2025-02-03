package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.customexceptions.FlutterWavePaymentException;
import com.ecommerce.nunda.customexceptions.PaymentException;
import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.service.PaymentMoMoService;
import com.flutterwave.bean.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class PaymentController {

    private final PaymentMoMoService paymentMoMoService;

    public PaymentController(PaymentMoMoService paymentMoMoService) {
        this.paymentMoMoService = paymentMoMoService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/payments")
    public String getPayment(){
        return "payment/payment";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/initiate-payment")
    public void processUserOrder(HttpSession session,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Principal principal
                                 ) throws IOException {

        BillingDetailsDTO billingDetails = (BillingDetailsDTO) session.getAttribute("billingDetails");
        Orders order = (Orders) session.getAttribute("order");

        Response responseFromFlutterwave = paymentMoMoService.makeMoMoPayment(billingDetails, request.getRemoteAddr(), order);

        if (responseFromFlutterwave == null) {
            throw new FlutterWavePaymentException("Payment failed due third please try again!");
        }

        // Clear session attributes
        session.removeAttribute("billingDetails");
        session.removeAttribute("order");

        // Redirect to external payment page
        response.sendRedirect(responseFromFlutterwave.getMeta().getAuthorization().getRedirect());
    }




}
