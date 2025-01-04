package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Cart;

public interface CartService {
    void  saveCart(Cart cart);

    Cart createCart();
}
