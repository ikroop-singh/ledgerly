package com.ledgerly.app.expense_tracker.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException exc){
        return ResponseEntity.status(404).body("Account not found for the user");
    }
}
