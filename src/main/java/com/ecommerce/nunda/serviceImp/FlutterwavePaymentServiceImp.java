package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.paymentutilities.ServiceProviderDetector;
import com.ecommerce.nunda.service.PaymentAuditService;
import com.ecommerce.nunda.service.PaymentMoMoService;
import com.flutterwave.bean.Response;
import com.flutterwave.bean.UgandaMobileMoneyRequestRequest;
import com.flutterwave.services.MobileMoney;
import com.flutterwave.utility.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FlutterwavePaymentServiceImp implements PaymentMoMoService {

    private static final Logger logger = LoggerFactory.getLogger(FlutterwavePaymentServiceImp.class);

    private final String SECRET_KEY;
    private final String PUBLIC_KEY;
    private final String ENCRYPTION_KEY;
    private final TransactionRefGenerator trg;
    private final PaymentAuditService paymentAuditService;
    private final ServiceProviderDetector serviceProviderDetector;

    public FlutterwavePaymentServiceImp(@Value("${flutterwave.secret.key}") String secretKey,
                                        @Value("${flutterwave.public.key}") String publicKey,
                                        @Value("${flutterwave.encryption.key}") String encryptionKey,
                                        TransactionRefGenerator trg,
                                        PaymentAuditService paymentAuditService,
                                        ServiceProviderDetector serviceProviderDetector) {
        this.SECRET_KEY = secretKey;
        this.PUBLIC_KEY = publicKey;
        this.ENCRYPTION_KEY = encryptionKey;
        this.trg = trg;
        this.paymentAuditService = paymentAuditService;
        this.serviceProviderDetector = serviceProviderDetector;
    }

    @Override
    public Response makeMoMoPayment(BillingDetailsDTO billingDetailsDTO, String IPAddress, Orders order) {
        logger.info("Executing makeMoMoPayment()");

        String transactionRef = trg.generateTransactionRef();
        logger.info("Generated TransactionRef: {}", transactionRef);

        paymentAuditService.auditIncomingRequest(transactionRef, billingDetailsDTO, order);
        logger.info("Payment request audited successfully");

        Environment.setSecretKey(SECRET_KEY);
        Environment.setPublicKey(PUBLIC_KEY);
        Environment.setEncryptionKey(ENCRYPTION_KEY);
        logger.info("Flutterwave API keys set");

        logger.info("Initiating Uganda Mobile Money Transaction...");
        Response paymentResponse = new MobileMoney()
                .runUgandaMobileMoneyTransaction(new UgandaMobileMoneyRequestRequest(
                        transactionRef,
                        new BigDecimal(billingDetailsDTO.getAmount()),
                        "UGX",
                        "143256743",
                        serviceProviderDetector.detectProvider(billingDetailsDTO.getPhoneNumber()),
                        billingDetailsDTO.getEmail(),
                        billingDetailsDTO.getPhoneNumber(),
                        billingDetailsDTO.getFullName(),
                        IPAddress,
                        "62wd23423rq324323qew1",
                        Optional.empty()
                ));

        logger.info("Mobile Money transaction completed");

        return paymentResponse;
    }
}



