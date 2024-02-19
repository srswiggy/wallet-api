package com.swiggy.wallet.exceptions;

import com.swiggy.wallet.responsemodels.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SameUserException.class)
    public ResponseEntity<ErrorResponse> handleSameUserException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("Both Users are same", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernamenotFound(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("No such user exists", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("Insufficient funds for transfer", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidDateRange.class)
    public ResponseEntity<ErrorResponse> handleInvalidDateRange(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("The Date Range entered was invalid", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
