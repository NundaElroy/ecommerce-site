package com.ecommerce.nunda.service;

import com.ecommerce.nunda.formvalidators.ReviewForm;

public interface ReviewService {
    void saveReview(ReviewForm reviewForm, Long product_id);
}
