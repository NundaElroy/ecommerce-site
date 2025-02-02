package com.ecommerce.nunda.service;

import com.ecommerce.nunda.dto.PayLoadDTO;

public interface PaymentMoMoService {

    public void makeMoMoPayment(PayLoadDTO payLoadDTO,String IPAddress);

}
