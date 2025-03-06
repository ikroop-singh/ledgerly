package com.ledgerly.app.expense_tracker.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BudgetExceptionHandler {

    private static final Logger logger= LoggerFactory.getLogger(BudgetExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<String >budgetNotFoundException(BudgetNotFoundException exception){

        return ResponseEntity.status(404).body("No budget is found for the user");

    }
}
