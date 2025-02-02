package com.ecommerce.nunda.serviceImp;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * This class is responsible for generating a unique transaction reference
 * Example TXN2025013114302598743
 */


@Service
public class TransactionRefGenerator {

    // Generate a unique transaction reference
    public  String generateTransactionRef() {
        // Get current timestamp in "yyyyMMddHHmmss" format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());

        // Generate a 5-digit random number
        int randomNum = 10000 + new Random().nextInt(90000); // Ensures a 5-digit number

        // Create the final transaction reference
        return "TXN" + timestamp + randomNum;
    }


}

