package com.ecommerce.nunda.service;

import com.ecommerce.nunda.dto.PayLoadDTO;

public interface PaymentAuditService {

    public void auditIncomingRequest(String transactionRef, PayLoadDTO payLoadDTO);
}
