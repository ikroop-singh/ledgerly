package com.ledgerly.app.expense_tracker.dao.transaction;

import com.ledgerly.app.expense_tracker.entity.Transaction;
import com.ledgerly.app.expense_tracker.entity.User;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionDao {

    public void deleteTransactions(List<Integer>ids);
    List<Transaction> fetchTransactionsByAccountId(int accountId);
    BigDecimal getTotalExpense(int accountId, int userId, LocalDate startDate, LocalDate endDate);
    void createTransaction(Transaction data);
    Transaction fetchTransactionByIdAndUserId(int transactionId, int userId);
    Transaction updateTransaction(Transaction transaction);
    List<Transaction> fetchUserTransactions(int userId);
    List<Transaction> fetchTransactionsDue(Transaction.RecurringInterval interval, LocalDateTime today);
    void recurringTransactionUpdate(Transaction transaction);
    List<Transaction> fetchMonthlyTransactions(LocalDate startDate,LocalDate endDate);

}
