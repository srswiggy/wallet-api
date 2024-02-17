package com.swiggy.wallet.exceptions;

public class SameUserException extends Exception{
    public SameUserException() {
        super("Both the users are same");
    }
}
