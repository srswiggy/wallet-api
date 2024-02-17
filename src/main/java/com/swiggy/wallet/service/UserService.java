package com.swiggy.wallet.service;

import com.swiggy.wallet.exceptions.InsufficientFundsException;
import com.swiggy.wallet.exceptions.SameUserException;
import com.swiggy.wallet.model.Currency;
import com.swiggy.wallet.model.User;
import com.swiggy.wallet.model.Wallet;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.requestmodels.UserDepositRequest;
import com.swiggy.wallet.requestmodels.UserTransferMoneyRequest;
import com.swiggy.wallet.requestmodels.UserWithdrawalRequest;
import com.swiggy.wallet.requestmodels.UserRegisterRequest;
import com.swiggy.wallet.responsemodels.UserDepositResponse;
import com.swiggy.wallet.responsemodels.UserTransferMoneyResponse;
import com.swiggy.wallet.responsemodels.UserWithdrawalResponse;
import com.swiggy.wallet.responsemodels.UserRegisterResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<UserRegisterResponse> createNewUser(UserRegisterRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setWallet(new Wallet(Currency.INR));
        repository.save(user);

        return ResponseEntity.ok(new UserRegisterResponse());
    }

    public ResponseEntity<List<User>> listAllUsers() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<UserWithdrawalResponse> withdraw(UserWithdrawalRequest request, String username) throws InsufficientFundsException {
        User user = repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User with " + username + " not found!"));
        Wallet wallet = user.getWallet();
        wallet.debit(request);
        repository.save(user);
        return ResponseEntity.ok(new UserWithdrawalResponse(request.getAmount()));
    }

    public ResponseEntity<UserDepositResponse> deposit(UserDepositRequest request, String username) {
        User user = repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User with " + username + " not found!"));
        Wallet wallet = user.getWallet();
        wallet.credit(request);
        repository.save(user);
        return ResponseEntity.ok(new UserDepositResponse(request.getAmount()));
    }

    @Transactional
    public ResponseEntity<UserTransferMoneyResponse> transfer(UserTransferMoneyRequest request, String senderUsername) throws InsufficientFundsException, SameUserException {
        User sendUser = repository.findByUsername(senderUsername).orElseThrow(()-> new UsernameNotFoundException("User with " + senderUsername + " not found!"));
        User receiveUser = repository.findByUsername(request.getReceiverUsername()).orElseThrow(()-> new UsernameNotFoundException("User with " + request.getReceiverUsername() + " not found!"));
        if(sendUser.getId().equals(receiveUser.getId())) throw new SameUserException();
        Wallet sendUserWallet = sendUser.getWallet();
        Wallet receiveUserWallet = receiveUser.getWallet();
        sendUserWallet.debit(request.getMoney());
        receiveUserWallet.credit(request.getMoney());
        repository.save(sendUser);
        repository.save(receiveUser);
        return ResponseEntity.ok(new UserTransferMoneyResponse(senderUsername, receiveUser.getUsername(), request.getMoney()));
    }
}
