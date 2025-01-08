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
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.nunda.serviceImp.JacksonService;
import com.ecommerce.nunda.service.CookieService;

import java.security.Principal;
import java.util.*;

@RestController
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private  final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final JacksonService jacksonService;
    private final CookieService cookieService;

    public CartController(CartService cartService, ProductService productService, CartItemService cartItemService, UserService userService, JacksonService jacksonService, CookieService cookieService) {
        this.cartService = cartService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.jacksonService = jacksonService;
        this.cookieService = cookieService;
    }

    @PostMapping("/addItemToCart")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public ResponseEntity<?> addItemToCart(@RequestBody Map<String, String> requestBody, Principal principal) {

        Long productId = parseProductId(requestBody.get("productId"));
        cartService.addProductToUserCart(principal.getName(), productId);

        return ResponseEntity.ok().body(createErrorResponse("Product added to cart", HttpStatus.OK));
    }






    @PostMapping("/guest/add")
    public ResponseEntity<?>  addiTemToCartForNonAuthedUsers(@RequestBody Map<String, String> requestBody ,
                                                             @CookieValue(value = "usercart",required = false) String usercart,
                                                             HttpServletResponse resp,
                                                             HttpServletRequest req) throws JsonProcessingException {


        Long productId = parseProductId(requestBody.get("productId"));
        String updatedCart = cartService.addProductToGuestCart(usercart, productId);

        // Create and update cookie
        Cookie cartCookie = cookieService.createCartCookie(updatedCart);
        resp.addCookie(cartCookie);

        logger.info("Product {} successfully added to cart for a non-authenticated user", productId);
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








}
