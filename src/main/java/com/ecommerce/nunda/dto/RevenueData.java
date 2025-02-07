package com.ecommerce.nunda.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevenueData {

    private double totalRevenue;


    public RevenueData(double totalRevenue) {
        this.totalRevenue = totalRevenue;

    }

    // Getters and Setters
}