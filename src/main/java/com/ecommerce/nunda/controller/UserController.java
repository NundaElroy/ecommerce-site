package com.ecommerce.nunda.controller;


import com.ecommerce.nunda.customexceptions.UserNotFoundException;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
    private final WishlistService wishlistService;

    public UserController(CategoryService categoryService, ProductService productService, CartService cartService, UserService userService, JacksonService jacksonService, CookieService cookieService, WishlistService wishlistService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.cartService = cartService;
        this.userService = userService;
        this.jacksonService = jacksonService;
        this.cookieService = cookieService;
        this.wishlistService = wishlistService;
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

        //handling cart items for
        // non-authenticated users
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
    public String getUserWishList(Model model,Principal principal) {
        if (principal == null) {
            return  "redirect:/login";
        }
        User user = userService.getUserByEmail(principal.getName()).
                                                        orElseThrow(() -> new UserNotFoundException("User not found"));

        model.addAttribute("wishlistItems",user.getWishlist().getWishlistItems());
        return "user/wishlist";
    }

    @PostMapping("/removeItemFromWishList")
    public String removeItemFromWishlIst(
            @RequestParam("product_id") Long product_id,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        // TODO: Return an error page instead of redirecting to login
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        wishlistService.removeProductFromWishlist(user.getWishlist(), product_id);

        // Add a success message as a redirect attribute
        redirectAttributes.addFlashAttribute("successMessage", "Product removed from wishlist successfully.");

        return "redirect:/wishlist";
    }


    @PostMapping("/addItemToCartFromWishlist")
    public String addItemToCart(
            @RequestParam("product_id") Long product_id,
            Principal principal,
            RedirectAttributes redirectAttributes){

        if (principal == null) {
            return "redirect:/login";
        }

        // TODO: Return an error page instead of redirecting to login
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Add the product to the user's cart
        cartService.addProductToUserCart(user.getEmail(), product_id);

        //remove it from wishlist
        wishlistService.removeProductFromWishlist(user.getWishlist(), product_id);

        // Add a success message as a redirect attribute
        redirectAttributes.addFlashAttribute("successMessage", "Product added to cart successfully.");

        return "redirect:/wishlist";
    }








}
