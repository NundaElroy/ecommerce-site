package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemService  extends JpaRepository<OrderItem,Long> {
}
