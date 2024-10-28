package com.ecommerce.nunda.security;

import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        private final UserService userService;


        public CustomUserDetailsService(UserService userService) {
            this.userService = userService;
        }

        @Override
        public UserDetails loadUserByUsername(String email) {
            User user = userService.getUserByEmail(email)  // Retrieve user from your service
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Create and return an instance of CustomUser
            return new CustomUser(user);
        }
    }


