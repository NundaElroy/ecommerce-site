package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage,Long> {
//    @Query("SELECT i.imagePath FROM ProductImage i WHERE i.product.id = :productId")
//    List<ProductImage> getAllProductImagesByProductId(@Param("productId")Long id);
    @Query("SELECT i FROM ProductImage i WHERE i.product.id = :productId")
    List<ProductImage> getAllProductImagesByProductId(@Param("productId") Long id);
}
