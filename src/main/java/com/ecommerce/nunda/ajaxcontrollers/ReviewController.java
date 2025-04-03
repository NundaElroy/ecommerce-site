package com.ecommerce.nunda.ajaxcontrollers;


import com.ecommerce.nunda.dto.ResponseDTO;
import com.ecommerce.nunda.dto.ReviewRequest;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.Review;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.repository.ReviewRepo;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final ProductService productService;
    private final UserService userService;
    private final ReviewRepo reviewRepo;

    public ReviewController(ProductService productService, UserService userService, ReviewRepo reviewRepo) {
        this.productService = productService;
        this.userService = userService;
        this.reviewRepo = reviewRepo;
    }

    @PostMapping("/submit-review")
    public ResponseEntity<ResponseDTO<?>> submitReview(@RequestBody ReviewRequest request, Principal principal) {
        logger.info("Received review submission request for Product ID: {}", request.getProductId());

        if (principal == null) {
            logger.warn("Unauthorized review submission attempt.");
            return ResponseEntity.badRequest().body(ResponseDTO.error("User must be authenticated to submit a review."));
        }

        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> {
                    logger.error("User with email {} not found.", principal.getName());
                    return new RuntimeException("User not found");
                });

        Product product = productService.getProductById(request.getProductId());
        logger.info("User {} is submitting a review for Product ID: {}", user.getEmail(), product.getProduct_id());

        if (request.getRating() < 1 || request.getRating() > 5) {
            logger.warn("Invalid rating {} by user {}", request.getRating(), user.getEmail());
            return ResponseEntity.badRequest().body(ResponseDTO.error("Invalid rating. Must be between 1 and 5."));
        }

        Double averageRating = productService.getAverageRating(product, request.getRating());
        if (averageRating == null) {
            logger.error("Failed to calculate average rating for Product ID: {}", product.getProduct_id());
            return ResponseEntity.badRequest().body(ResponseDTO.error("Failed to calculate rating"));
        }
        logger.info("Updated average rating for Product ID {}: {}", product.getProduct_id(), averageRating);

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setReview(request.getReview());
        review.setEmail(user.getEmail());
        review.setName(user.getFirstName()+ " " + user.getLastName());

        reviewRepo.save(review);
        logger.info("Review successfully submitted by user {} for Product ID {}", user.getEmail(), product.getProduct_id());

        return ResponseEntity.ok(ResponseDTO.success("Review submitted successfully!", averageRating));
    }
}

