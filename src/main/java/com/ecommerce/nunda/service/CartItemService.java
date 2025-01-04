package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.entity.Product;

public interface CartItemService {


    void addItemToCart(Cart cart, Product productById);
}
