package com.ecommerce.nunda.controller;



import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.service.CartItemService;
import com.ecommerce.nunda.service.CartService;
import com.ecommerce.nunda.service.CategoryService;
import com.ecommerce.nunda.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
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

    public GlobalAdviceAttributes(CategoryService categoryService, UserService userService, CartService cartService, CartItemService cartItemService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    // Add global attributes to all views
    @ModelAttribute("categories")
    public List<Category> populateCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("cartCount")
    public  int populateCartCount(Principal principal){
         if(principal != null){
             Optional<User> user = userService.getUserByEmail(principal.getName());
             Cart cart = user.get().getCart();
             if (cart == null) {
                 return 0;
             }

             return  cart.getCartItemList().size();
         }

            return 0;
    }

}
