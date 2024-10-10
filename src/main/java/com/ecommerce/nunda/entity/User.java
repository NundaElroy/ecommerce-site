package com.ecommerce.nunda.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String role;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Cart cart;

    User(){}

    public void addOrder(Order order){
        orders.add(order);
        order.setUser(this);
    }
    //handling the bidirectional relationship
    public void removeOrder(Order order){
        orders.remove(order);
        order.setUser(null);
    }
    public void setCart(Cart cart) {
        this.cart = cart;
        if (cart != null) {
            cart.setUser(this);  // Sync the relationship
        }
    }

    public void removeCart() {
        if (cart != null) {
            cart.setUser(null);  // Remove the sync
            this.cart = null;
        }
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
