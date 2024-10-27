package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductService extends JpaRepository<Product,Long> {
}
