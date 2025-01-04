package com.ecommerce.nunda.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cart_item_id;
    private Integer quantity = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public CartItem(){}

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        cart.addCartItem(this);
        this.cart = cart;
    }

    public Long getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(Long cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        product.addCartItem(this);
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
