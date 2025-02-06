package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT COUNT(user_id) FROM `user` WHERE role = 'ROLE_USER'", nativeQuery = true)
    long getTotalNumberOfCustomers();

}
