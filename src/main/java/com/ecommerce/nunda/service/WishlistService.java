package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.entity.Wishlist;

public interface WishlistService {
    void addProductToUserWishlist(String name, Long productId);

    Wishlist createWishlist(User user);
}
