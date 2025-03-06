package com.ledgerly.app.expense_tracker.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DatabaseExceptionHandler {

    private static final Logger logger= LoggerFactory.getLogger(DatabaseExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<String >handleDatabaseException(DatabaseException exception){
        logger.error("Exception occurred "+exception.getMessage());
        return ResponseEntity.status(500).body("Internal error occurred occurred while processing your request ");

    }
}

