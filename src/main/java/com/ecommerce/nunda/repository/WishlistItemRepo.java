package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepo extends JpaRepository<WishlistItem, Long> {
}
