package com.ecommerce.nunda.service;

import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;

public interface PaymentAuditService {

    public void auditIncomingRequest(String transactionRef, BillingDetailsDTO payLoadDTO, Orders order);
}
