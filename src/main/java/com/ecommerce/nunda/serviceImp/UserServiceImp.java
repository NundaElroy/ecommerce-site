package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.repository.UserRepo;
import com.ecommerce.nunda.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}



