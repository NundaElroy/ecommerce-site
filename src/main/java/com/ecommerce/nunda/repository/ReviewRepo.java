package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review,Long> {
}
