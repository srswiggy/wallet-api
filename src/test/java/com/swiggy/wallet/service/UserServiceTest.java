package com.swiggy.wallet.service;

import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.requestmodels.UserRegisterRequest;
import com.swiggy.wallet.responsemodels.UserRegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp(){
        openMocks(this);
    }
    @Test
    void testIfUserServiceGettingCreated() {
        assertDoesNotThrow(UserService::new);
    }

    @Test
    void testIfCreateUserReturnsUserRegisterResponse() {
        UserRegisterRequest userRequest = spy(new UserRegisterRequest("user", "pass"));
        UserRegisterResponse response = userService.createNewUser(userRequest).getBody();

        assertNotNull(response);
        assertEquals(response, new UserRegisterResponse());
        verify(userRepository, times(1)).save(any());
        verify(userRequest, times(1)).getPassword();
        verify(userRequest, times(1)).getUsername();
    }
}