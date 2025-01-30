package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.dto.CartItemsDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CartService {
    void  saveCart(Cart cart);

    Cart createCart();

    Cart getUserCart(Long user_id);

    List<CartItem> convertCookieListToCartItems(List<String> cartProductsInCookieCart);

    void addProductToUserCart(String name, Long productId);

    String addProductToGuestCart(String usercart, Long productId) throws JsonProcessingException;

    List<CartItemsDto>  convertCartItemsToCartItemsDTO(List<CartItem> cartItemList);

    void removeProductFromCart(Cart cart, Long productId);

    boolean checkIfProductsExistAndQuantityIsSufficient(List<CartItemsDto> items );


    boolean changeCartStatus(String email, List<CartItemsDto> items);

    String removeProductFromGuestCart(String userCart,Long  product_id) throws JsonProcessingException;

    void moveCookieCartItemsToCartForRegisteringUser(String userCart,String email) throws JsonProcessingException;
}
