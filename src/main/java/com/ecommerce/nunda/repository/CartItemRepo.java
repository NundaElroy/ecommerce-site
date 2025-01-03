package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo  extends JpaRepository<CartItem,Long> {
}
