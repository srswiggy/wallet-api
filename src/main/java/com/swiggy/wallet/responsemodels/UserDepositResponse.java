package com.swiggy.wallet.responsemodels;

import lombok.Data;

@Data
public class UserDepositResponse {
    private String message = "Deposit Success";
    private double depositAmount;

    public UserDepositResponse(double amount) {
        this.depositAmount = amount;
    }
}
