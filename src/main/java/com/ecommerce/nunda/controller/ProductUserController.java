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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String hotDeals() {
        // Get all active promotions
//        model.addAttribute("products", productService.getActivePromotions());
        return "product/hotdeals";
    }

//get products by category
@GetMapping("/category/{category_name}/{id}")
public String viewCategoryProducts(
        @PathVariable("category_name") String categoryName,
        @PathVariable("id") Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "9") int size,
        Model model) {
    // Fetch paginated products for the given category
    Page<Product> productPage = productService.getProductsByCategory(page, size,id);

    // Add data to the model
    // Pass data to the model
    model.addAttribute("products", productPage.getContent()); // Current page's products
    model.addAttribute("totalProducts", productPage.getTotalElements()); // Total number of products
    model.addAttribute("totalPages", productPage.getTotalPages()); // Total pages
    model.addAttribute("currentPage", productPage.getNumber()); // Current page number (0-based)
    model.addAttribute("size", productPage.getSize()); // Page size
    model.addAttribute("category_name", categoryName); // Category name
    model.addAttribute("id", id); // Category ID


    return "product/hotdeals"; // View name

    }

//view individual products
    @GetMapping("/view_product/{id}")
    public String viewProduct(@PathVariable("id")Long id,Model model){
        Product product = productService.getProductById(id);

        //model.addAttribute("review", new ReviewForm());
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
