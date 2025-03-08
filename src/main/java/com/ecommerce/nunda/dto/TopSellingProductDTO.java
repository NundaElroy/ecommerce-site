package com.ecommerce.nunda.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;


public class TopSellingProductDTO {
//    private Long productId;
    private String productImage;
    private String name;
    private Double price;
    private Long sold;
    private Double revenue;

    public TopSellingProductDTO(String productImage, String name, Double price, Long sold, Double revenue) {
        this.productImage = productImage;
        this.name = name;
        this.price = price;
        this.sold = sold;
        this.revenue = revenue;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Long getSold() {
        return sold;
    }

    public void setSold(Long sold) {
        this.sold = sold;
    }



    public TopSellingProductDTO(){}


}
