package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.Review;
import com.ecommerce.nunda.formvalidators.ReviewForm;
import com.ecommerce.nunda.service.CategoryService;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.ReviewService;
import com.ecommerce.nunda.serviceImp.ProductServiceImp;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductUserController {
    private final ProductService productService;
    private final ReviewService reviewService;
    private static final Logger logger = LoggerFactory.getLogger(ProductUserController.class);

    public ProductUserController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("/hotdeals")
    public String hotDeals(Model model) {
        // Get all active promotions
        model.addAttribute("products", productService.getActivePromotions());
        return "product/hotdeals";
    }
//view individual products
    @GetMapping("/view_product/{id}")
    public String viewProduct(@PathVariable("id")Long id,Model model){
        Product product = productService.getProductById(id);

//        model.addAttribute("review", new ReviewForm());
        model.addAttribute("product", product);
        // Get related products
        model.addAttribute("relatedProducts", productService.getRelatedProducts(product));

        return "product/viewindividualproduct";
    }

    @PostMapping("/view_product/addReview/{id}")
    public String addReview(
            @PathVariable Long id,
            @ModelAttribute("review") @Valid ReviewForm review,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
                 result.getAllErrors().forEach(error -> {
                logger.error(error.getDefaultMessage());
            });
            model.addAttribute("product", productService.getProductById(id));
            return "redirect:/view_product/" + id; // Redirect back to the form page
        }

        reviewService.saveReview(review, id);
        logger.debug("Review added successfully for product ID: {}", id);
        return "redirect:/view_product/" + id;
    }

}
