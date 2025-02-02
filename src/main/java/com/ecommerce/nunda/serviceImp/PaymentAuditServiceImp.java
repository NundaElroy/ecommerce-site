package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.dto.PayLoadDTO;
import com.ecommerce.nunda.entity.Payment;
import com.ecommerce.nunda.repository.PaymentRepo;
import com.ecommerce.nunda.service.PaymentAuditService;
import org.springframework.stereotype.Service;


@Service
public class PaymentAuditServiceImp implements PaymentAuditService {


    private final PaymentRepo paymentRepo;

    public PaymentAuditServiceImp(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }


    @Override
    public void auditIncomingRequest(String transactionRef, PayLoadDTO payLoadDTO) {

        Payment payment = new Payment();
        payment.setAmount(Double.valueOf(payLoadDTO.getAmount()));
        payment.setTransactionRef(transactionRef);

        paymentRepo.save(payment);

    }
}
