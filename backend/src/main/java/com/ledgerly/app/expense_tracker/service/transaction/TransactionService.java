package com.ledgerly.app.expense_tracker.service.transaction;


import com.ledgerly.app.expense_tracker.DTO.TransactionRequestDTO;
import com.ledgerly.app.expense_tracker.entity.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void deleteTransactions(List<Integer> ids);
    List<Transaction> fetchTransactionsByAccountId(int accountId);
    void createTransaction(TransactionRequestDTO data);
    Transaction updateTransaction(TransactionRequestDTO data,int transactionId);
    Transaction  fetchTransactionByIdAndUserId(int transactionId);
    List<Transaction>fetchUserTransactions();
    List<Transaction> getDueTransactions(Transaction.RecurringInterval interval, LocalDateTime today);
    void recurringTransactionUpdate(Transaction transaction);
    List<Transaction>fetchMonthlyTransactions();
}
