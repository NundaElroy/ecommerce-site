package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.User;
import com.ecommerce.nunda.repository.UserRepo;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserServiceImpTest {
     private  UserServiceImp underTest;
     private AutoCloseable autoCloseable;
     @Mock
     private UserRepo userRepo;

   ///each test we get a new instance during each service


    //runs before each test
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserServiceImp(userRepo);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }
    @DisplayName("Junit test for getUserByEmail")
    @Test
    void getUserByEmail_ShouldReturnEmptyOptional_WhenUserDoesNotExist() {
        //given
        String email = "johndoe@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        //when
        Optional<User> userTest = underTest.getUserByEmail(email);

        //then
        assertThat(userTest.isPresent()).isTrue();
        assertThat(userTest.get().getEmail()).isEqualTo(email);
        verify(userRepo).findByEmail("johndoe@example.com");
    }


    /*
    we have to test for the cases that is when the user input is valid ,password and email is null
    ,email already in use
    so in that case three scenarios
    so for the first case when they are valid
     */
    @Test
    void registerUser_ShouldSaveUser_WhenValidInput() {
        //given(pre condtions)
        String email = "johndoe@example.com";
        String pass = "abc123";
        User user = new User();
        user.setEmail(email);
        user.setPassword(pass);

        //return empty optional
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());


        //when
        underTest.registerUser(user);


        //then
        verify(userRepo).save(user);


    }
    @Test
    void registerUser_ShouldThrowException_WhenEmailIsNull() {
        // Given
        User user = new User();
        user.setPassword("password123");
        //when//then
        assertThatThrownBy(()-> underTest.registerUser(user)).isInstanceOf(IllegalArgumentException.class);

    }
    @Test
    void registerUser_ShouldThrowException_WhenPasswordIsNull() {
        // Given
        User user = new User();
        user.setEmail("johndoe@example.com");
        //when//then
        assertThatThrownBy(()-> underTest.registerUser(user)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailIsAlreadyInUse() {
        // Given
        String email = "johndoe@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password123");

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user)); // Simulate existing user

        // When/Then
        assertThatThrownBy(() -> underTest.registerUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email is already in use");
    }



    @Test
    void getAllUsers_shouldReturnAllUsers() {
        //when
        underTest.getAllUsers();
        //then
        verify(userRepo).findAll();



    }
}