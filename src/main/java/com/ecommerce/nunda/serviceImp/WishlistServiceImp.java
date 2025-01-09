package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.entity.Wishlist;
import com.ecommerce.nunda.repository.WishlistRepo;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.UserService;
import com.ecommerce.nunda.service.WishlistItemService;
import com.ecommerce.nunda.service.WishlistService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishlistServiceImp implements WishlistService {

    private final UserService userService;
    private final ProductService productService;
    private final WishlistItemService wishlistItemService;
    private final WishlistRepo wishlistRepo;
    private final static Logger logger = LoggerFactory.getLogger(WishlistServiceImp.class);

    public WishlistServiceImp(UserService userService, ProductService productService, WishlistItemService wishlistItemService, WishlistRepo wishlistRepo) {
        this.userService = userService;
        this.productService = productService;
        this.wishlistItemService = wishlistItemService;
        this.wishlistRepo = wishlistRepo;
    }

    @Override
    @Transactional
    public void addProductToUserWishlist(String userEmail, Long productId)  {
        User user = userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        logger.info("User {} is adding product {} to wishlist", userEmail, productId);

        Wishlist wishlist = Optional.ofNullable(user.getWishlist())
                .orElseGet(() -> createWishlist(user));

        Product product = productService.getProductById(productId,"WishlistServiceImp");

        if (wishlistItemService.isProductInWishlist(wishlist, product)) {
            throw new IllegalArgumentException("Product already in wishlist");
        }

        wishlistItemService.addItemToWishlist(wishlist, product);
        logger.info("Product {} successfully added to wishlist for user {}", productId, userEmail);

    }

    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        user.setWishlist(wishlist);
        logger.info("Creating a wishlist for {}", user.getEmail());
        return wishlistRepo.save(wishlist);
    }
}
