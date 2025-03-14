package com.ecommerce.nunda.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class
CartItemsDto {

    private Long product_id;

    private String name;

    private Integer stockQuantity;

    private Integer quantity = 1;

    private String productImage;

    private String description;

    private Double discountPercentage;

    private LocalDateTime discountEndTime;

    private Double price;


}

