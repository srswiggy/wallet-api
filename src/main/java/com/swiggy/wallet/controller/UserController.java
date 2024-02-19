package com.swiggy.wallet.controller;

import com.swiggy.wallet.exceptions.DuplicateUserException;
import com.swiggy.wallet.exceptions.InsufficientFundsException;
import com.swiggy.wallet.exceptions.InvalidDateRange;
import com.swiggy.wallet.exceptions.SameUserException;
import com.swiggy.wallet.model.User;
import com.swiggy.wallet.model.UserTransaction;
import com.swiggy.wallet.requestmodels.*;
import com.swiggy.wallet.responsemodels.UserDepositResponse;
import com.swiggy.wallet.responsemodels.UserTransferMoneyResponse;
import com.swiggy.wallet.responsemodels.UserWithdrawalResponse;
import com.swiggy.wallet.responsemodels.UserRegisterResponse;
import com.swiggy.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("https://localhost:3000/")
public class UserController {
    @Autowired
    UserService userService;
    @CrossOrigin(origins = "http://localhost:3000/")
    @PostMapping("/register")
    ResponseEntity<UserRegisterResponse> createUser(@RequestBody UserRegisterRequest request) throws DuplicateUserException {
        return userService.createNewUser(request);
    }

    @GetMapping("/users")
    ResponseEntity<List<User>> listAllUsers() {
        return userService.listAllUsers();
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @PutMapping("/withdraw")
    ResponseEntity<UserWithdrawalResponse> withdraw(@RequestBody UserWithdrawalRequest request) throws InsufficientFundsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.withdraw(request, username);
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @PutMapping("/deposit")
    ResponseEntity<UserDepositResponse> withdraw(@RequestBody UserDepositRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.deposit(request, username);
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @PutMapping("/transfer")
    ResponseEntity<UserTransferMoneyResponse> withdraw(@RequestBody UserTransferMoneyRequest request) throws InsufficientFundsException, SameUserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderUsername = authentication.getName();
        return userService.transfer(request, senderUsername);
    }

    @CrossOrigin(origins = "http://localhost:3000/")
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> userTransactions(
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) throws InvalidDateRange {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();

        if(toDate == null) toDate = LocalDate.now();
        if(fromDate == null) fromDate = LocalDate.EPOCH;

        return userService.userTransactions(currentUser, toDate, fromDate);
    }
}
