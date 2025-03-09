package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.entity.Payment;
import com.ecommerce.nunda.repository.PaymentRepo;
import com.ecommerce.nunda.service.PaymentAuditService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PaymentAuditServiceImp implements PaymentAuditService {


    private final PaymentRepo paymentRepo;

    public PaymentAuditServiceImp(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }


    @Override
    public void auditIncomingRequest(String transactionRef, BillingDetailsDTO payLoadDTO, Orders order) {

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(Double.valueOf(payLoadDTO.getAmount()));
        payment.setTransactionRef(transactionRef);

        paymentRepo.save(payment);

    }

    @Override
    public List<Payment> getAllPaymentInfo(){
        return paymentRepo.findAll();
    }
}
