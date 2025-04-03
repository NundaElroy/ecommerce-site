package com.ecommerce.nunda.ajaxcontrollers;

import com.ecommerce.nunda.customexceptions.FlutterWavePaymentException;
import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.service.PaymentMoMoService;
import com.flutterwave.bean.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RestController;


@RestController
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentMoMoService paymentMoMoService;

    public PaymentController(PaymentMoMoService paymentMoMoService) {
        this.paymentMoMoService = paymentMoMoService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/initiate-payment")
    public void processUserOrder(HttpSession session,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Principal principal) throws IOException {

        logger.info("Initiating payment for user: {}", principal.getName());

        BillingDetailsDTO billingDetails = (BillingDetailsDTO) session.getAttribute("billingDetails");
        Orders order = (Orders) session.getAttribute("order");

        if (billingDetails == null || order == null) {
            logger.warn("Missing session attributes for payment processing - User: {}", principal.getName());
            throw new IllegalStateException("Required session attributes missing.");
        }

        logger.info("Retrieved session details - User: {}, Order ID: {}", principal.getName(), order.getOrder_id());

        logger.info("Calling MoMo payment service for User: {} with IP: {}", principal.getName(), request.getRemoteAddr());
        Response responseFromFlutterwave = paymentMoMoService.makeMoMoPayment(billingDetails, request.getRemoteAddr(), order);

        if (responseFromFlutterwave == null) {
            logger.error("Payment failed for User: {} due to null response from Flutterwave", principal.getName());
            throw new FlutterWavePaymentException("Payment failed due to an issue with the payment provider. Please try again.");
        }

        logger.info("Payment initiated successfully for User: {}, Redirecting to: {}", principal.getName(), responseFromFlutterwave.getMeta().getAuthorization().getRedirect());

        // Clear session attributes after processing
        session.removeAttribute("billingDetails");
        session.removeAttribute("order");
        logger.info("Session attributes cleared for User: {}", principal.getName());

        // Redirect to external payment page
        response.sendRedirect(responseFromFlutterwave.getMeta().getAuthorization().getRedirect());
    }
}
