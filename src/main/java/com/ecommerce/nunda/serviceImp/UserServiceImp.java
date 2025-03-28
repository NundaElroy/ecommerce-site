package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.entity.OrderItem;
import com.ecommerce.nunda.enums.OrderStatus;
import com.ecommerce.nunda.repository.UserRepo;
import com.ecommerce.nunda.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp  implements UserService {
    private final UserRepo userRepo;



    public UserServiceImp(UserRepo userRepo) {
        this.userRepo = userRepo;

    }

    //get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email); // Ensure this returns Optional<User>
    }

    //register user
    public void registerUser(User user) {



        if (user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        }

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }



        user.setCreatedAt(LocalDateTime.now());
        userRepo.save(user);



    }

    //get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Override
    public User returnSavedUser(User user) {
        return  userRepo.save(user);
    }

    @Override
    public boolean checkIfUserEmailAlreadyExists(String email){
         return userRepo.findByEmail(email).isPresent();
    }


    @Override
    public  List<Orders> getAllCustomerOrders(String email){
        User user = getUserByEmail(email).orElseThrow(() -> {
            throw new UserNotFoundException("User no Found");
        });

        return  user.getOrders();
    }

    @Override
    public long getAllCustomers(){
        return userRepo.getTotalNumberOfCustomers();
    }


    @Override
    public List<Product> getAllProductsForReview(String email) {
        User user = getUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not Found")
        );

        return user.getOrders().stream()
                .filter(order -> order.getStatus().equals(OrderStatus.COMPLETE))
                .map(Orders::getOrderItems)                // Stream<List<OrderItem>>
                .flatMap(List::stream)                     // Stream<OrderItem>
                .map(OrderItem::getProduct)                // Stream<Product>
                .collect(Collectors.toList());
    }
}



