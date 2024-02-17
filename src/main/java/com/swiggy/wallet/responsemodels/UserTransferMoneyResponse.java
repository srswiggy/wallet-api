package com.swiggy.wallet.responsemodels;

import com.swiggy.wallet.model.Currency;
import com.swiggy.wallet.model.Money;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserTransferMoneyResponse {
    private String senderUsername;
    private String receiverUsername;
    private double amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public UserTransferMoneyResponse(String senderUsername, String receiverUsername, Money money) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.amount = money.getAmount();
        this.currency = money.getCurrency();
    }
}
