package com.swiggy.wallet.exceptions;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super("Wallet has insufficient Funds");
    }
}
