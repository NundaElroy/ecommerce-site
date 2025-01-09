package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.Wishlist;

public interface WishlistItemService {
    boolean isProductInWishlist(Wishlist wishlist, Product product);

    void addItemToWishlist(Wishlist wishlist, Product product);
}
