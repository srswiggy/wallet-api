package com.swiggy.wallet.controller;

import com.swiggy.wallet.exceptions.InsufficientFundsException;
import com.swiggy.wallet.exceptions.SameUserException;
import com.swiggy.wallet.model.User;
import com.swiggy.wallet.requestmodels.UserDepositRequest;
import com.swiggy.wallet.requestmodels.UserTransferMoneyRequest;
import com.swiggy.wallet.requestmodels.UserWithdrawalRequest;
import com.swiggy.wallet.requestmodels.UserRegisterRequest;
import com.swiggy.wallet.responsemodels.UserDepositResponse;
import com.swiggy.wallet.responsemodels.UserTransferMoneyResponse;
import com.swiggy.wallet.responsemodels.UserWithdrawalResponse;
import com.swiggy.wallet.responsemodels.UserRegisterResponse;
import com.swiggy.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    ResponseEntity<UserRegisterResponse> createUser(@RequestBody UserRegisterRequest request) {
        return userService.createNewUser(request);
    }

    @GetMapping("/users")
    ResponseEntity<List<User>> listAllUsers() {
        return userService.listAllUsers();
    }

    @PutMapping("/withdraw")
    ResponseEntity<UserWithdrawalResponse> withdraw(@RequestBody UserWithdrawalRequest request) throws InsufficientFundsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.withdraw(request, username);
    }

    @PutMapping("/deposit")
    ResponseEntity<UserDepositResponse> withdraw(@RequestBody UserDepositRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.deposit(request, username);
    }

    @PutMapping("/transfer")
    ResponseEntity<UserTransferMoneyResponse> withdraw(@RequestBody UserTransferMoneyRequest request) throws InsufficientFundsException, SameUserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderUsername = authentication.getName();
        return userService.transfer(request, senderUsername);
    }
}
