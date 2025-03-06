package com.ledgerly.app.expense_tracker.service;


import com.ledgerly.app.expense_tracker.dao.account.AccountDao;
import com.ledgerly.app.expense_tracker.entity.Account;
import com.ledgerly.app.expense_tracker.entity.Transaction;
import com.ledgerly.app.expense_tracker.exceptions.DatabaseException;
import com.ledgerly.app.expense_tracker.service.account.AccountService;
import com.ledgerly.app.expense_tracker.service.transaction.TransactionService;
import com.ledgerly.app.expense_tracker.service.transaction.TransactionServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionSchedulerService {

    TransactionService transactionService;
    AccountDao accountDao;
    private static final Logger logger= LoggerFactory.getLogger(TransactionSchedulerService.class) ;

    @Scheduled(cron = "0 0 0 * * ?") // Daily at 00:00
    public void processDailyTransaction(){
        processTransactionByInterval(Transaction.RecurringInterval.DAILY, LocalDateTime.now());
    }
    @Scheduled(cron = "0 0 0 1 * ?") // Every 1st of the month
    public void processMonthlyTransaction(){
        processTransactionByInterval(Transaction.RecurringInterval.DAILY, LocalDateTime.now());
    }
    @Scheduled(cron = "0 0 0 * * MON") // Every Monday
    public void processWeeklyTransaction(){
        processTransactionByInterval(Transaction.RecurringInterval.DAILY, LocalDateTime.now());
    }
    @Scheduled(cron = "0 0 0 1 1 ?") // Every January 1st
    public void processYearlyTransaction(){
        processTransactionByInterval(Transaction.RecurringInterval.DAILY, LocalDateTime.now());
    }

    LocalDateTime calculateNextRecurring(Transaction.RecurringInterval interval){

        LocalDateTime nextDateTime=LocalDateTime.now();
        switch (interval){

            case DAILY : nextDateTime=nextDateTime.plusDays(1);
                          break;
            case WEEKLY: nextDateTime=nextDateTime.plusWeeks(1);

            case MONTHLY:nextDateTime= nextDateTime.plusMonths(1);

            case YEARLY: nextDateTime=nextDateTime.plusYears(1);

             default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }
        return nextDateTime;
    }
    public void processTransactionByInterval(Transaction.RecurringInterval interval, LocalDateTime date){
        List<Transaction>transactionsDue=transactionService.getDueTransactions(interval,date);

        for(Transaction transaction:transactionsDue){
            try{
                processTransaction(transaction);
            }
            catch(DataAccessException exception){
                logger.error("Exception occurred while processing the recurring transaction for {}",transaction.getId());
                throw new DatabaseException("Exception occurred while processing the recurring transaction"+exception);
            }
        }

    }

    @Transactional
    public void processTransaction(Transaction transaction){
        //logic to process transactions

        //fetch account details
        Account account=transaction.getAccount();
        BigDecimal currentBalance=account.getBalance();
        BigDecimal updatedBalance=transaction.getType().equals(Transaction.Type.EXPENSE)?currentBalance.subtract(transaction.getAmount()):currentBalance.add(transaction.getAmount());
        account.setBalance(updatedBalance);
        accountDao.updateAccount(account);

        //Update transaction last processed field
        transaction.setLastProcessed(LocalDateTime.now());

        transaction.setStatus(Transaction.Status.COMPLETED);


        LocalDateTime nextRecurring=calculateNextRecurring(transaction.getRecurringInterval());
        transaction.setNextRecurringDate(nextRecurring);

        transactionService.recurringTransactionUpdate(transaction);
    }
}
