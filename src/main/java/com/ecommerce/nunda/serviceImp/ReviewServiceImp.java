package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.Review;
import com.ecommerce.nunda.formvalidators.ReviewForm;
import com.ecommerce.nunda.repository.ReviewRepo;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImp implements ReviewService {
    private final ReviewRepo reviewRepo;
    private final ProductService productService;


    public ReviewServiceImp(ReviewRepo reviewRepo, ProductService productService) {
        this.reviewRepo = reviewRepo;
        this.productService = productService;
    }

    //save review
    @Override
    public void saveReview(ReviewForm reviewForm,Long product_id) {
        Review review = new Review();
        review.setName(reviewForm.getName());
        review.setEmail(reviewForm.getEmail());
        review.setReview(reviewForm.getReviewText());
        review.setRating(reviewForm.getRating());
        reviewRepo.save(review);
        calculateRating(product_id);
    }

//Calculate the average rating of a product
    private void calculateRating(Long productId) {
        // Fetch the product by ID
        Product product = productService.getProductById(productId);

        // Ensure product exists and has reviews
        if (product != null && product.getReviews() != null && !product.getReviews().isEmpty()) {
            // Calculate average rating
            double averageRating = product.getReviews()
                    .stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0); // Default to 0.0 if no ratings

            // Update product's rating if needed
            product.setRatings(averageRating); // Assuming Product has an averageRating field
            productService.saveProduct(product); // Persist the updated product
        } else {
            System.out.println("No reviews available for the product.");
        }
    }

}
