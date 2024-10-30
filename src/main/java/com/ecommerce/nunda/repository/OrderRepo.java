package com.ecommerce.nunda.repository;



import com.ecommerce.nunda.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo  extends JpaRepository<Orders,Long> {
}
