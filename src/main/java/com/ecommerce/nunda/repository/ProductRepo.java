package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE p.category.category_id = :categoryId")
    Page<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    //select products less that are less than three months old
    @Query(value = "SELECT * FROM product WHERE created_at >= CURRENT_DATE - INTERVAL 3 MONTH ORDER BY RAND()", nativeQuery = true)
    Page <Product> getNewProducts(Pageable pageable);

}
