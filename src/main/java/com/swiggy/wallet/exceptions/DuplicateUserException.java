package com.swiggy.wallet.exceptions;

public class DuplicateUserException extends Exception{
    public DuplicateUserException() {
        super("Duplicate User");
    }
}
