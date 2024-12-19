package com.ecommerce.nunda.formvalidators;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class PromotionsDTO {

    @NotNull(message = "product id cant be null")
    private Long productId;

    @NotNull(message = "discount percentange cant be null")
    @Min(value = 0 , message = "discount cant be less that 0")
    @Max(value= 100,message = "discount cant be greater than 100")
    private Double discountPercentage;

    @NotNull(message = "start time is required")
    @FutureOrPresent(message = "start time cannot be in the past")
    private LocalDateTime discountStartTime;

    @NotNull(message = "end  time is required")
    @FutureOrPresent(message = "end time cannot be in the past")
    private LocalDateTime discountEndTime;

    public LocalDateTime getDiscountEndTime() {
        return discountEndTime;
    }

    public void setDiscountEndTime(LocalDateTime discountEndTime) {
        this.discountEndTime = discountEndTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public LocalDateTime getDiscountStartTime() {
        return discountStartTime;
    }

    public void setDiscountStartTime(LocalDateTime discountStartTime) {
        this.discountStartTime = discountStartTime;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }


}
