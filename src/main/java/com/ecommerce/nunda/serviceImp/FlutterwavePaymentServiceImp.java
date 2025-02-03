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


@Service
public class FlutterwavePaymentServiceImp implements PaymentMoMoService {

    private final String SECRET_KEY ;
    private final String PUBLIC_KEY ;
    private final String ENCRYPTION_KEY ;
    private final TransactionRefGenerator trg;
    private final PaymentAuditService paymentAuditService;
    private final ServiceProviderDetector serviceProviderDetector;



    public FlutterwavePaymentServiceImp(@Value("${flutterwave.secret.key}")String secretKey,
                                        @Value("${flutterwave.public.key}")String publicKey,
                                        @Value("${flutterwave.encryption.key}")String encryptionKey, TransactionRefGenerator trg, PaymentAuditService paymentAuditService, ServiceProviderDetector serviceProviderDetector) {
        this.SECRET_KEY = secretKey;
        this.PUBLIC_KEY = publicKey;
        this.ENCRYPTION_KEY = encryptionKey;
        this.trg = trg;
        this.paymentAuditService = paymentAuditService;
        this.serviceProviderDetector = serviceProviderDetector;
    }


    @Override
    public  Response makeMoMoPayment(BillingDetailsDTO billingDetailsDTO, String IPAddress, Orders order) {
        String transactionRef = trg.generateTransactionRef();

        //To do save payment details to the database
        paymentAuditService.auditIncomingRequest(transactionRef,billingDetailsDTO,order);


        Environment.setSecretKey(SECRET_KEY);
        Environment.setPublicKey(PUBLIC_KEY);
        Environment.setEncryptionKey(ENCRYPTION_KEY);

        return new MobileMoney()
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
    }

}


