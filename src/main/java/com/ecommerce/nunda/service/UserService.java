package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.formvalidators.UserForm;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByEmail(String email);
     void  registerUser(User user);
     List<User> getAllUsers();
     void  saveUser(User user);


    boolean checkIfUserEmailAlreadyExists(String email);
}
