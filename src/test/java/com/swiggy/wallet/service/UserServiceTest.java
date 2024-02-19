package com.swiggy.wallet.service;

import com.swiggy.wallet.exceptions.DuplicateUserException;
import com.swiggy.wallet.model.User;
import com.swiggy.wallet.repository.TransactionRepository;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.requestmodels.TransactionResponse;
import com.swiggy.wallet.requestmodels.UserRegisterRequest;
import com.swiggy.wallet.responsemodels.UserRegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    TransactionRepository transactionRepository;
    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        openMocks(this);
    }
    @Test
    void testIfUserServiceGettingCreated() {
        assertDoesNotThrow(UserService::new);
    }

    @Test
    void testIfCreateUserReturnsUserRegisterResponse() throws DuplicateUserException {
        UserRegisterRequest userRequest = spy(new UserRegisterRequest("user", "pass"));
        UserRegisterResponse response = userService.createNewUser(userRequest).getBody();

        assertNotNull(response);
        assertEquals(response, new UserRegisterResponse());
        verify(userRepository, times(1)).findByUsername("user");
        verify(userRepository, times(1)).save(any());
        verify(userRequest, times(1)).getPassword();
        verify(userRequest, times(2)).getUsername();
    }

    @Test
    void testIfCreateUserReturnsUsernameAlreadyPresentException() throws DuplicateUserException {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateUserException.class, () -> userService.createNewUser(
                new UserRegisterRequest("user", "pass")
        ));

        verify(userRepository, times(1)).findByUsername("user");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testTractionsReturnsListOfTransactionsResponse() {
        User user = new User(1, "user", "pass");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        ResponseEntity<List<TransactionResponse>> transactionResponses = userService.userTransactions("user");
        assertNotNull(transactionResponses);
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(transactionRepository, times(1)).findAllBySenderId(anyLong());
        verify(transactionRepository, times(1)).findAllByReceiverId(anyLong());
    }
}