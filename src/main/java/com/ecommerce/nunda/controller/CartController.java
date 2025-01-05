package com.ecommerce.nunda.controller;


import com.ecommerce.nunda.customexceptions.ProductNotFoundException;
import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.service.CartItemService;
import com.ecommerce.nunda.service.CartService;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private  final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    public CartController(CartService cartService, ProductService productService, CartItemService cartItemService, UserService userService) {
        this.cartService = cartService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }



//    @PostMapping("/addItemToCart")
//    @PreAuthorize("hasRole('ROLE_USER')")  // Ensure user has correct role
//    @Transactional
//    public String addItemToCart(@RequestParam("id") Long product_id, Principal principal) {
//
//        Optional<User> user = userService.getUserByEmail(principal.getName());
//
//        if (user.isEmpty()) {
//            throw new UserNotFoundException("User not found");
//        }
//
//        // Validate product exists and is active
//        Product product = productService.getProductById(product_id);
//        if (product == null) {
//            throw new ProductNotFoundException("Product not found or inactive");
//        }
//
//        // Check if cart exists, create if needed
//        Cart cart = user.get().getCart();
//        if ( cart == null){
//            user.get().setCart( cartService.createCart());
//        }
//
//
//        cartItemService.addItemToCart(cart, product);
//
//        return "redirect:/cart";
//    }

    @PostMapping("/addItemToCart")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public ResponseEntity<?> addItemToCart(@RequestBody Map<String, String> requestBody, Principal principal) {

        String productIdString = requestBody.get("productId");
        if (productIdString == null) {
            return ResponseEntity.badRequest().body(createErrorResponse("Product ID is missing", HttpStatus.BAD_REQUEST));
        }

        Long productId;
        try {
            productId = Long.parseLong(productIdString);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid product ID", HttpStatus.BAD_REQUEST));
        }

        logger.info("User {} is adding product {} to cart", principal.getName(), productId);

        Optional<User> user = userService.getUserByEmail(principal.getName());
        if (user.isEmpty()) {
            logger.error("User not found for email: {}", principal.getName());
            throw new UserNotFoundException("User not found");
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            logger.warn("Product {} not found or inactive", productId);
            throw new ProductNotFoundException("Product not found or inactive");
        }

        Cart cart = user.get().getCart();
        if (cart == null) {
            logger.info("Creating a new cart for user {}", principal.getName());
            user.get().setCart(cartService.createCart());
            cart = user.get().getCart();
        }

        //check if product is already in cart
        if (cartItemService.isProductInCart(cart, product)) {
            logger.info("Product {} is already in cart for user {}", productId, principal.getName());
            return ResponseEntity.badRequest().body(createErrorResponse("Product already in cart", HttpStatus.BAD_REQUEST));
        }

        cartItemService.addItemToCart(cart, product);

        logger.info("Product {} successfully added to cart for user {}", productId, principal.getName());

        return ResponseEntity.ok().body(createErrorResponse("Product added to cart", HttpStatus.OK));
    }




    /*
    * Helper method to create a structured error response
    *
    * @return Map<String, Object> errorResponse
    *        {
             "status": 404,
             "message": "Product not found or inactive"
              }
    *
    *
    * */
    private Map<String, String > createErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(status.value()));
        errorResponse.put("message", message);
        return errorResponse;
    }

}
