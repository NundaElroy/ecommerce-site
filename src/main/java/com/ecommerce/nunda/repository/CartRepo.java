package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.Cart;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepo extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.user_id = :userId")
    Cart findByCartUserId(@Param("userId") Long userId);
}
