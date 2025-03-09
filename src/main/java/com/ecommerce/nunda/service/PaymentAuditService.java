package com.ecommerce.nunda.service;

import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.entity.Payment;

import java.util.List;

public interface PaymentAuditService {

    public void auditIncomingRequest(String transactionRef, BillingDetailsDTO payLoadDTO, Orders order);

    List<Payment> getAllPaymentInfo();
}
