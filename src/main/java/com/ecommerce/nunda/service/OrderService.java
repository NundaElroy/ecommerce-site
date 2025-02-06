package com.ecommerce.nunda.service;

import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.dto.SalesData;
import com.ecommerce.nunda.entity.Orders;

import java.util.List;

public interface OrderService {
    List<Orders> getAllOrders();
    Orders createOrder( String email, BillingDetailsDTO billingDetailsDTO);


    //getting sales
    SalesData getSalesByPeriod(String period);
}
