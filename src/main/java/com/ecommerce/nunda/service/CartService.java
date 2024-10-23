package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartService extends JpaRepository<Cart,Long> {
}
