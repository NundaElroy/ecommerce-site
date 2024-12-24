package com.ecommerce.nunda.formvalidators;

import jakarta.validation.constraints.*;
import lombok.Data;


public class ReviewForm {

    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Review is required")
    private String reviewText;
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;

    public ReviewForm() {
    }

    public ReviewForm(String name, String email, String review, Integer rating) {
        this.name = name;
        this.email = email;
        this.reviewText = review;
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
