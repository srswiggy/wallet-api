package com.swiggy.wallet.responsemodels;


import lombok.Data;

@Data
public class UserWithdrawalResponse {
    private String message = "Withdrawal Success";
    private double withdrawalAmount;

    public UserWithdrawalResponse(double amount) {
        this.withdrawalAmount = amount;
    }
}
