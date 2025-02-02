package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.dto.PayLoadDTO;
import com.ecommerce.nunda.entity.Payment;
import com.ecommerce.nunda.repository.PaymentRepo;
import com.ecommerce.nunda.service.PayloadEncryptor;
import com.ecommerce.nunda.service.PaymentAuditService;
import com.ecommerce.nunda.service.PaymentMoMoService;
import com.flutterwave.bean.Response;
import com.flutterwave.bean.UgandaMobileMoneyRequestRequest;
import com.flutterwave.services.MobileMoney;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FlutterwavePaymentServiceImp implements PaymentMoMoService {

    private final String SECRET_KEY ;
    private final String PUBLIC_KEY ;
    private final String ENCRYPTION_KEY ;
    private final PayloadEncryptor payloadEncryptor;
    private final TransactionRefGenerator trg;
    private final PaymentAuditService paymentAuditService;



    public FlutterwavePaymentServiceImp(@Value("${flutterwave.secret.key}")String secretKey,
                                        @Value("${flutterwave.public.key}")String publicKey,
                                        @Value("${flutterwave.encryption.key}")String encryptionKey, PayloadEncryptor payloadEncryptor, TransactionRefGenerator trg, PaymentAuditService paymentAuditService) {
        this.SECRET_KEY = secretKey;
        this.PUBLIC_KEY = publicKey;
        this.ENCRYPTION_KEY = encryptionKey;
        this.payloadEncryptor = payloadEncryptor;
        this.trg = trg;

        this.paymentAuditService = paymentAuditService;
    }


    @Override
    public void makeMoMoPayment(PayLoadDTO payLoadDTO,String IPAddress) {
        String transactionRef = trg.generateTransactionRef();

        //To do save payment details to the database
        paymentAuditService.auditIncomingRequest(transactionRef,payLoadDTO);


        Response flwResponse=new MobileMoney()
                .runUgandaMobileMoneyTransaction(new UgandaMobileMoneyRequestRequest(
                        transactionRef,
                        new BigDecimal(payLoadDTO.getAmount()),
                        "UGX",
                        "143256743",
                        "MTN",
                        payLoadDTO.getEmail(),
                        "054709929220",
                        payLoadDTO.getFullName(),
                        IPAddress,
                        "62wd23423rq324323qew1",
                        Optional.empty()
                ));

    }

}


//  Environment.setSecretKey("FLWSECK_TEST-92e*******-X");
//  Environment.setPublicKey("FLWSECK_TEST-92e*******-X");
//  Environment.setEncryptionKey("FLWSECK_TE********c1f2");