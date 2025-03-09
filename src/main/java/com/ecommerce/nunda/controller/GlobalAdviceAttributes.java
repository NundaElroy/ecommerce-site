package com.ecommerce.nunda.controller;



import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.entity.Wishlist;
import com.ecommerce.nunda.service.*;
import com.ecommerce.nunda.serviceImp.JacksonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalAdviceAttributes {

    private final CategoryService categoryService;
    private final UserService userService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final JacksonService jacksonService;
    private final CookieService cookieService;
    private static final Logger logger = LoggerFactory.getLogger(GlobalAdviceAttributes.class);

    public GlobalAdviceAttributes(CategoryService categoryService, UserService userService, CartService cartService, CartItemService cartItemService, JacksonService jacksonService, CookieService cookieService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.jacksonService = jacksonService;
        this.cookieService = cookieService;
    }

    // Add global attributes to all views
    @ModelAttribute("categories")
    public List<Category> populateCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("cartCount")
    public  int populateCartCount(Principal principal,
                                  @CookieValue(value = "usercart",required = false) String usercart) throws JsonProcessingException {
         //authenticated user
         if(principal != null){
             Optional<User> user = userService.getUserByEmail(principal.getName());
             Cart cart = user.get().getCart();
             if (cart == null) {
                 return 0;
             }
             int size = cart.getCartItemList().size();
         logger.info("Number of products in cart: {}", size);
         return  size;
         }

         //non authenticated user
         if (usercart == null) {
             logger.info("No cookie cart found");
             return 0;
         }

         List<String> numberOfProductsInCookieCart =  jacksonService.convertStringCookieToList(cookieService.decodeCookie(usercart));

         int size = numberOfProductsInCookieCart.size();
         logger.info("Number of products in cookie cart: {}", size);
         return size;
    }

    @ModelAttribute("wishlistCount")
    public int populateWishlistCount(Principal principal){
        //authenticated user
        if(principal == null){
            //Todo:return error page not authorized
            return 0;
        }


        assert principal != null;
        Optional<User> user = userService.getUserByEmail(principal.getName());
        Wishlist wishlist = user.get().getWishlist();
        if (wishlist == null) {
            return 0;
        }
        int size = wishlist.getWishlistItems().size();
        logger.info("Number of products in wishlist: {}", size);
        return  size;


    }


    @ModelAttribute("username")
    public String getAdminName(Principal principal){

        if(principal == null){
            return "Guest";
        }

        User user = userService.getUserByEmail(principal.getName()).orElseThrow(()->
                new UserNotFoundException("User not found"));


        return user.getFirstName() + " " + user.getLastName();
    }

}
