package com.ecommerce.nunda.paymentutilities;

import com.ecommerce.nunda.customexceptions.PaymentException;
import org.springframework.stereotype.Service;


@Service
public class ServiceProviderDetector {

    public  String detectProvider(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.startsWith("+256")) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        String numberWithoutCode = phoneNumber.substring(4); // Remove +256

        if (numberWithoutCode.startsWith("77") || numberWithoutCode.startsWith("78")) {
            return "MTN";
        } else if (numberWithoutCode.startsWith("75") || numberWithoutCode.startsWith("70")) {
            return "AIRTEL";
        }

        throw new PaymentException("service provider not supported");
    }


}

