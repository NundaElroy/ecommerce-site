package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.customexceptions.ProductNotFoundException;
import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.repository.CartRepo;
import com.ecommerce.nunda.service.CartItemService;
import com.ecommerce.nunda.service.CartService;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CartServiceImp implements CartService {

    private final CartRepo cartRepo;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImp.class);

    public CartServiceImp(CartRepo cartRepo, CartItemService cartItemService, UserService userService, ProductService productService) {
        this.cartRepo = cartRepo;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepo.save(cart);
    }

    @Override
    public Cart createCart() {
        logger.info("Creating a new cart for user ");
        return new Cart();
    }

    @Override
    public Cart getUserCart(Long user_id) {
//        Cart cart = cartRepo.findByUserId(user_id);
//        if (cart == null) {
//            cart = createCart();
//            cartRepo.save(cart);
//        }

        return null;
    }


    //convert to cookie cart items to cartitems
    public List<CartItem> convertCookieListToCartItems(List<String> cartProductsInCookieCart){

         return cartProductsInCookieCart.stream()
                                        .map((productid) -> {
                                              return cartItemService.createCartItem(Long.valueOf(productid));
                                         })
                                        .collect(Collectors.toList()) ;

    }

    @Override
    @Transactional
    public void addProductToUserCart(String userEmail, Long productId)  {
        User user = userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        logger.info("User {} is adding product {} to cart", userEmail, productId);

        Cart cart = Optional.ofNullable(user.getCart())
                .orElseGet(this::createCart);

        Product product = productService.getProductById(productId);
        if (product == null) {
            logger.warn("Product {} not found or inactive", productId);
            throw new ProductNotFoundException("Product not found or inactive");
        }

        if (cartItemService.isProductInCart(cart, product)) {
            throw new IllegalArgumentException("Product already in cart");
        }

        cartItemService.addItemToCart(cart, product);
        logger.info("Product {} successfully added to cart for user {}", productId, userEmail);

    }


}
