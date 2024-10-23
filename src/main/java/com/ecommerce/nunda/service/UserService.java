package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService extends JpaRepository<User,Long> {
}
