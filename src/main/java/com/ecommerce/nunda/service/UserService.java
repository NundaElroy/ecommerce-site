package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByEmail(String email);
     void  registerUser(User user);
     List<User> getAllUsers();
     void  saveUser(User user);
     User  returnSavedUser(User user);


    boolean checkIfUserEmailAlreadyExists(String email);

    List<Orders> getAllCustomerOrders(String email);
}
