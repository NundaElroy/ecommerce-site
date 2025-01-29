package com.ecommerce.nunda.formvalidators;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartFormDTO {
    private List<CartItemsDto> cartItems = new ArrayList<>();

    public void setCartItems(List<CartItemsDto> cartItems) {
        this.cartItems = cartItems;
    }
}
