package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

}
