package com.ecommerce.nunda.repository;

import com.ecommerce.nunda.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    void findByEmail() {
        // given
        String email = "johndoe@example.com";
        User user = new User();
        user.setEmail(email);
        userRepo.save(user);  // Save user to the repository for testing

        // when
        boolean userExists = userRepo.findByEmail(email).isPresent();

        // then
        assertThat(userExists).isTrue();

        // Clean up if necessary
        userRepo.delete(user);
    }

}