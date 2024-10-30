package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByEmail(String email);
     void  registerUser(User user);
     List<User> getAllUsers();
}
