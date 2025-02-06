package com.ecommerce.nunda.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesData {

    private long totalOrders;


    public SalesData(long totalOrders) {
        this.totalOrders = totalOrders;

    }

    // Getters and Setters
}

