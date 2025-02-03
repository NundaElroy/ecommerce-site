package com.ecommerce.nunda.service;

import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.Orders;
import com.flutterwave.bean.Response;

public interface PaymentMoMoService {

    public Response makeMoMoPayment(BillingDetailsDTO payLoadDTO, String IPAddress, Orders order);

}
