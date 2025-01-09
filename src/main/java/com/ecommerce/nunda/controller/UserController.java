package com.ecommerce.nunda.controller;


import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.service.*;
import com.ecommerce.nunda.serviceImp.JacksonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CartService cartService;
    private final UserService userService;
    private final JacksonService jacksonService;
    private final CookieService cookieService;

    public UserController(CategoryService categoryService, ProductService productService, CartService cartService, UserService userService, JacksonService jacksonService, CookieService cookieService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.cartService = cartService;
        this.userService = userService;
        this.jacksonService = jacksonService;
        this.cookieService = cookieService;
    }

    //guest page for non auth users
    @GetMapping({"/guest","","/home"})
    public String index(Model model ) {

        Page<Product> page = productService.getNewProduct(0,5);
        model.addAttribute("newProducts",page.getContent());
        return "user/guest_user" ;
    }

    //when viewing cart
    @GetMapping("/cart")
    public String getUserCart(Model model, Principal principal,
                              @CookieValue(value = "usercart",required = false) String usercart) throws JsonProcessingException {

        //handling cart items for non authenticated users
        if (principal == null) {
            if(usercart == null) {
                return "user/cart";
            }

            List<CartItem> items = cartService.convertCookieListToCartItems(
                               jacksonService.convertStringCookieToList(cookieService.decodeCookie(usercart)));


            model.addAttribute("cartItems", items);
            return "user/cart";

        }



        //get user using principal
        Optional<User> user = userService.getUserByEmail(principal.getName());

        //check if user has cart
        Cart cart = user.get().getCart();
        if (cart == null){
            //create cart
            user.get().setCart(cartService.createCart());
            userService.saveUser(user.get());
        }


        model.addAttribute("cartItems", cart.getCartItemList());
        return "user/cart";
    }

    //when viewing wishlist
    @GetMapping("/wishlist")
    public String getUserWishList(Model model) {
        return "user/wishlist";
    }






}
