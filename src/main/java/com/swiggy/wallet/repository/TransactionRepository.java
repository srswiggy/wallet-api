package com.swiggy.wallet.repository;

import com.swiggy.wallet.model.UserTransaction;
import com.swiggy.wallet.requestmodels.TransactionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<UserTransaction, Long> {
//    @Query("SELECT t.id AS transaction_id, u_sender.username AS sender_username, u_receiver.username AS receiver_username, t.amount, t.timestamp FROM transactions t JOIN users u_sender ON t.sender_id = u_sender.id JOIN users u_receiver ON t.receiver_id = u_receiver.id WHERE :userId IN (t.sender_id, t.receiver_id)")
//    List<TransactionResponse> findTransctions(long userId);

    List<UserTransaction> findAllBySenderId(long senderId);
    List<UserTransaction> findAllByReceiverId(long receiverId);
}
