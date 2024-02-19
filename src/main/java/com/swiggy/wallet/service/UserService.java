package com.swiggy.wallet.service;

import com.swiggy.wallet.exceptions.DuplicateUserException;
import com.swiggy.wallet.exceptions.InsufficientFundsException;
import com.swiggy.wallet.exceptions.InvalidDateRange;
import com.swiggy.wallet.exceptions.SameUserException;
import com.swiggy.wallet.model.Currency;
import com.swiggy.wallet.model.User;
import com.swiggy.wallet.model.Wallet;
import com.swiggy.wallet.model.UserTransaction;
import com.swiggy.wallet.repository.TransactionRepository;
import com.swiggy.wallet.repository.UserRepository;
import com.swiggy.wallet.requestmodels.*;
import com.swiggy.wallet.responsemodels.UserDepositResponse;
import com.swiggy.wallet.responsemodels.UserTransferMoneyResponse;
import com.swiggy.wallet.responsemodels.UserWithdrawalResponse;
import com.swiggy.wallet.responsemodels.UserRegisterResponse;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository repository;
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<UserRegisterResponse> createNewUser(UserRegisterRequest userRequest) throws DuplicateUserException {
        Optional<User> userByUsername = repository.findByUsername(userRequest.getUsername());
        if(userByUsername.isPresent()) throw new DuplicateUserException();
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setWallet(new Wallet(Currency.INR));
        repository.save(user);

        return new ResponseEntity<>(new UserRegisterResponse(), HttpStatus.CREATED);
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
        UserTransaction newTransaction = new UserTransaction();
        newTransaction.setSender(sendUser);
        newTransaction.setReceiver(receiveUser);
        newTransaction.setMoney(request.getMoney());
        transactionRepository.save(newTransaction);
        return ResponseEntity.ok(new UserTransferMoneyResponse(senderUsername, receiveUser.getUsername(), request.getMoney()));
    }

    @Transactional
    public ResponseEntity<List<TransactionResponse>> userTransactions(String currentUser, LocalDate toDate, LocalDate fromDate) throws InvalidDateRange {
        if(fromDate.isAfter(toDate)) throw new InvalidDateRange();
        User sendUser = repository.findByUsername(currentUser).orElseThrow(()-> new UsernameNotFoundException("User with " + currentUser + " not found!"));
        List<UserTransaction> transactionsBySenderId = transactionRepository.findAllBySenderId(sendUser.getId());
        List<UserTransaction> transactionsByReceiverId = transactionRepository.findAllByReceiverId(sendUser.getId());

        List<UserTransaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(transactionsByReceiverId);
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        allTransactions.addAll(transactionsBySenderId);
        allTransactions.forEach(transaction -> {
            LocalDate localDate = transaction.getTimestamp().toLocalDateTime().toLocalDate();
            if(!localDate.isBefore(fromDate) && !localDate.isAfter(toDate))
                transactionResponses.add(new TransactionResponse(transaction));
        });

        transactionResponses.sort((t1, t2) -> Long.compare(t1.getTransactionId(), t2.getTransactionId()));

        return new ResponseEntity<>(transactionResponses, HttpStatus.OK);
    }
}
