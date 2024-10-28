package com.ecommerce.nunda.configs;

import com.ecommerce.nunda.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.GrantedAuthority;




@EnableWebSecurity
@Configuration
public class Security {
    private final CustomUserDetailsService customUserDetailsService;


    public Security(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    @Bean
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
//                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/login", "/register").permitAll() // Open routes
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin pages require ADMIN role
                .anyRequest().authenticated() // Restrict other routes
        )
                .formLogin(form -> form
                        .loginPage("/login") // Set your custom login page
                        .usernameParameter("email") // Use email instead of username
                        .passwordParameter("password") // Set password parameter
                        .successHandler((request, response, authentication) -> {
                            // Determine the role of the authenticated user
                            String role = authentication.getAuthorities().stream()
                                    .findFirst()
                                    .map(GrantedAuthority::getAuthority)
                                    .orElse("");

                            if (role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/admin/home"); // Redirect to admin dashboard
                            } else {
                                response.sendRedirect("/customer/home"); // Redirect to customer home
                            }
                        })// Redirect after successful login
                        .failureUrl("/login?error=true") // Redirect on failure
                        .permitAll() // Allow everyone to access the login page
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

