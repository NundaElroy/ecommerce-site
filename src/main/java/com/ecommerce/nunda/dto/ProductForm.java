package com.ecommerce.nunda.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class ProductForm {


        @NotBlank(message = "Product name is required.")
        private String name;

        @NotBlank(message = "Product description is required.")
        private String description;

        @NotNull(message = "Price is required.")
        private Double price;

        @NotNull(message = "Stock quantity is required.")
        @Min(value = 0, message = "Stock quantity cannot be negative.")
        private Integer stockQuantity;


        @NotNull(message = "Category is required.")
        private Long category;

    @NotNull(message = "Product image is required.", groups = OnAdd.class)
        private MultipartFile image;  // Store the image filename or path

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getStockQuantity() {
            return stockQuantity;
        }

        public void setStockQuantity(Integer stockQuantity) {
            this.stockQuantity = stockQuantity;
        }


        public Long getCategory() {
            return category;
        }

        public void setCategory(Long category) {
            this.category = category;
        }

        public  MultipartFile getImage() {
            return image;
        }

        public void setImage(MultipartFile image) {
            this.image = image;
        }
    }


