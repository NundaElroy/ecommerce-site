package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderService  extends JpaRepository<Orders,Long> {
}
