package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentService extends JpaRepository<Payment,Long> {
}
