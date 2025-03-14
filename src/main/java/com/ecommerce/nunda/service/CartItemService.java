package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.entity.Product;

public interface CartItemService {


    void addItemToCart(Cart cart, Product productById);

    boolean isProductInCart(Cart cart, Product product);

    CartItem createCartItem(Long productid);

    void removeItemFromCart(Cart cart, Product product);

    void updateCartItemQuantity(Cart cart, Product product, Integer quantity);
}
