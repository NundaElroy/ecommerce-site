package com.ecommerce.nunda.formvalidators;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartItemsDto {

    private Long product_id;

    private String name;

    private Integer stockQuantity;

    private Integer quantity = 1;

    private String productImage;

    private String description;

    private Double discountPercentage;

    private Double price;


}

