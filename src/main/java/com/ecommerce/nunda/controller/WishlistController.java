package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;


import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }


    @PostMapping("/addItemToWishlist")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addItemToCart(@RequestBody Map<String, String> requestBody, Principal principal) {

        Long productId = parseProductId(requestBody.get("productId"));
         wishlistService.addProductToUserWishlist(principal.getName(), productId);

        return ResponseEntity.ok().body(createErrorResponse("Product added to wishlist", HttpStatus.OK));
    }

    //@TO DO move to product service
    private Long parseProductId(String productIdString) {
        if (productIdString == null || productIdString.isBlank()) {
            throw new IllegalArgumentException("Product ID is missing");
        }
        try {
            return Long.parseLong(productIdString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid product ID format");
        }
    }
    //create an error handler service for this
    private Map<String, String > createErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(status.value()));
        errorResponse.put("message", message);
        return errorResponse;
    }
}
