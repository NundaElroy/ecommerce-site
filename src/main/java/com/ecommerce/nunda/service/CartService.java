package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;

import java.util.List;

public interface CartService {
    void  saveCart(Cart cart);

    Cart createCart();

    Cart getUserCart(Long user_id);

    List<CartItem> convertCookieListToCartItems(List<String> cartProductsInCookieCart);

    void addProductToUserCart(String name, Long productId);
}
