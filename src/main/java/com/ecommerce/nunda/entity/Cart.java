package com.ecommerce.nunda.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cart_id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItemList;


    Cart(){}

    public void addCartItem(CartItem cartItem){
        cartItemList.add(cartItem);
        cartItem.setCart(this);
    }

    public void removeCartItem(CartItem cartItem){
        cartItemList.remove(cartItem);
        cartItem.setCart(null);
    }

    public Long getCart_id() {
        return cart_id;
    }

    public void setCart_id(Long cart_id) {
        this.cart_id = cart_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
