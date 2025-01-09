package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.*;
import com.ecommerce.nunda.repository.WishlistItemRepo;
import com.ecommerce.nunda.service.WishlistItemService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WishlistItemServiceImp implements WishlistItemService {

    private final WishlistItemRepo wishlistItemRepo;
    private final static Logger logger = LoggerFactory.getLogger(WishlistItemServiceImp.class);

    public WishlistItemServiceImp(WishlistItemRepo wishlistItemRepo) {
        this.wishlistItemRepo = wishlistItemRepo;
    }

    @Override
    public boolean isProductInWishlist(Wishlist wishlist, Product product) {
        if (wishlist == null || wishlist.getWishlistItems() == null || product == null) {
            return false;
        }

        return wishlist.getWishlistItems().stream()
                .anyMatch(wishlistItem -> wishlistItem.getProduct().equals(product));

    }

    @Override
    @Transactional
    public  void addItemToWishlist(Wishlist wishlist, Product product) {
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.addWishlist(wishlist);
        wishlistItem.setProduct(product);
        wishlistItemRepo.save(wishlistItem);
        logger.info("Product added to wishlist: {}", product.getName());

    }
}
