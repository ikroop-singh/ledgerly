package com.ledgerly.app.expense_tracker.exceptions;

public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException(String message){
         super(message);
    }
}
