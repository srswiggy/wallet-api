package com.swiggy.wallet.requestmodels;

import com.swiggy.wallet.model.UserTransaction;
import com.swiggy.wallet.repository.TransactionRepository;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class TransactionResponse {
    private long transactionId;
    private String sender;
    private String receiver;
    private double amount;
    private Timestamp timestamp;

    public TransactionResponse(UserTransaction transaction) {
        this.transactionId = transaction.getId();
        this.sender = transaction.getSender().getUsername();
        this.receiver = transaction.getReceiver().getUsername();
        this.amount = transaction.getMoney().getAmount();
        this.timestamp = transaction.getTimestamp();
    }
}
