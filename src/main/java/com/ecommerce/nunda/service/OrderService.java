package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderService  extends JpaRepository<Orders,Long> {
}
