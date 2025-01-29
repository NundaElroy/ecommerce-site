package com.ecommerce.nunda.controller;


import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.formvalidators.CartFormDTO;
import com.ecommerce.nunda.formvalidators.CartItemsDto;
import com.ecommerce.nunda.service.*;
import com.ecommerce.nunda.serviceImp.JacksonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

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
        CartFormDTO cartFormDTO = new CartFormDTO();
        //handling cart items for
        // non-authenticated users
        if (principal == null) {
            if(usercart == null  || usercart.equals("")) {
                model.addAttribute("cartForm", cartFormDTO);
                return "user/cart";
            }

            List<CartItem> items = cartService.convertCookieListToCartItems(
                               jacksonService.convertStringCookieToList(cookieService.decodeCookie(usercart)));

            cartFormDTO.setCartItems(cartService.convertCartItemsToCartItemsDTO(items));

            model.addAttribute("cartForm", cartFormDTO);
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
            cart= user.get().getCart();
        }


        cartFormDTO.setCartItems(cartService.convertCartItemsToCartItemsDTO(cart.getCartItemList()));
        model.addAttribute("cartForm", cartFormDTO);
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


    @GetMapping("/checkout")
    public String getCheckout(){
        return "user/checkout";
    }


    @PostMapping("/removeItemFromCart")
    public String removeItemFromCart(
            @RequestParam("product_id") Long product_id,
            Principal principal,
            RedirectAttributes redirectAttributes,
            @CookieValue(value = "usercart",required = false) String usercart,
            HttpServletResponse resp
            ) throws JsonProcessingException {

        if (principal == null) {
            //Todo: handle for non auth user
            String newUserCart = cartService.removeProductFromGuestCart(usercart, product_id);
            Cookie  cookie = cookieService.createCartCookie(newUserCart);
            resp.addCookie(cookie);
            return "redirect:/cart";
        }

        // TODO: Return an error page instead of redirecting to login
        User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        cartService.removeProductFromCart(user.getCart(), product_id);

        // Add a success message as a redirect attribute
        redirectAttributes.addFlashAttribute("successMessage", "Product removed from cart successfully.");

        return "redirect:/cart";
    }



    @PostMapping("/removeItemFromWishList")
    public String removeItemFromWishlist(
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


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/usercheckout")
    public String checkout( @ModelAttribute("cartForm") CartFormDTO cartFormDTO, RedirectAttributes redirectAttributes,Principal principal) {

        // If no validation errors, proceed with the checkout process
        for (CartItemsDto item : cartFormDTO.getCartItems()) {
            logger.info("Product ID: {}", item.getProduct_id());
            logger.info("Quantity: {}", item.getQuantity());
        }

        //check if product quantity is greater than ten or ten items
        if (cartFormDTO.getCartItems().size() > 10 || cartFormDTO.getCartItems().stream().anyMatch(item -> item.getQuantity() > 10)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You can only order 10 items at a time.");
            return "redirect:/cart";
        }

        //first check if products exist, you can only order 10 items at a time and max ten
        if (!cartService.checkIfProductsExistAndQuantityIsSufficient(cartFormDTO.getCartItems())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product does not exist or quantity is insufficient.");
            return "redirect:/cart";
        }

        //so if the customer meets all the requirements  change there cart status to CHECKOUT from ACTIVE
        if (!cartService.changeCartStatus(principal.getName(), cartFormDTO.getCartItems() )) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error occurred while processing your request.");
            return "redirect:/cart";
        }



        return "redirect:/checkout";

    }









}
