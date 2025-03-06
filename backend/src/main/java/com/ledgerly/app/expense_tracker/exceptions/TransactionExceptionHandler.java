package com.ledgerly.app.expense_tracker.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(TransactionExceptionHandler.class);
    @ExceptionHandler
    public ResponseEntity<String> handleTransactionException(TransactionNotFoundException exc){
        logger.error(exc.getMessage());
        return ResponseEntity.status(404).body("Transactions not found for some IDS");
    }
}
