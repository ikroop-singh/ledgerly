package com.ledgerly.app.expense_tracker.service.transaction;

import com.ledgerly.app.expense_tracker.DTO.TransactionRequestDTO;
import com.ledgerly.app.expense_tracker.dao.account.AccountDao;
import com.ledgerly.app.expense_tracker.dao.transaction.TransactionDao;
import com.ledgerly.app.expense_tracker.entity.Account;
import com.ledgerly.app.expense_tracker.entity.Transaction;
import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.exceptions.DatabaseException;
import com.ledgerly.app.expense_tracker.exceptions.TransactionNotFoundException;
import com.ledgerly.app.expense_tracker.service.user.UserService;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private static final Logger logger= LoggerFactory.getLogger(TransactionServiceImpl.class) ;
    TransactionDao transactionDao;
    AccountDao accountDao;
    UserService userService;

    @Override
    public void deleteTransactions(List<Integer> ids) {
            transactionDao.deleteTransactions(ids);
    }

    @Override
    public     List<Transaction>    fetchTransactionsByAccountId(int accountId) {
        return transactionDao.fetchTransactionsByAccountId(accountId);
    }

    public LocalDateTime calculateNextDate(Transaction.RecurringInterval interval){
        LocalDateTime nextDateTime=LocalDateTime.now();
        switch (interval) {
            case DAILY:
                nextDateTime = nextDateTime.plusDays(1);
                break;
            case WEEKLY:
                nextDateTime = nextDateTime.plusWeeks(1);
                break;
            case MONTHLY:
                nextDateTime = nextDateTime.plusMonths(1);
                break;
            case YEARLY:
                nextDateTime= nextDateTime.plusYears(1);
            default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }
        return nextDateTime;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTransaction(TransactionRequestDTO data) {

        try{
            //fetch logged in user
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            User loggedInUser=userService.findUserName(authentication.getName());

            // Fetch account and update the balance
            Account account =accountDao.findAccountById(data.getAccountId());

            if(account==null){
                //throw Account not found exception
                throw new IllegalArgumentException("Account cannot be null");
            }

            BigDecimal newBalance=data.getType().equals(Transaction.Type.EXPENSE) ? account.getBalance().subtract(data.getAmount()):account.getBalance().add(data.getAmount());
            account.setBalance(newBalance);
            accountDao.updateAccount(account);

            // Add transaction
            Transaction transaction=new Transaction();
            transaction.setType(data.getType());
            transaction.setUser(loggedInUser);
            transaction.setAccount(account);
            transaction.setAmount(data.getAmount());
            transaction.setCategory(data.getCategory());
            transaction.setDescription(data.getDescription());
            transaction.setRecurring(data.isRecurring());
            transaction.setTime(data.getTime());
            transaction.setRecurringInterval(data.getRecurringInterval());
            if(data.isRecurring())
                    transaction.setStatus(Transaction.Status.PENDING);

            LocalDateTime nextRecurringDate=data.isRecurring() && data.getRecurringInterval()!=null ? calculateNextDate(data.getRecurringInterval()) :null;

            transaction.setNextRecurringDate(nextRecurringDate);
            transactionDao.createTransaction(transaction);
        }
        catch(DataAccessException exception){
            throw new DatabaseException("Error occurred while creating transaction");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction updateTransaction(TransactionRequestDTO data,int transactionId){
        try{

            //Fetch current transaction
            Transaction currentTransaction=fetchTransactionByIdAndUserId(transactionId);

            //Fetch account to update the balance
            Account account=accountDao.findAccountById(data.getAccountId());

            BigDecimal oldBalance=currentTransaction.getType().equals(Transaction.Type.INCOME)?account.getBalance().subtract(currentTransaction.getAmount()):account.getBalance().add(currentTransaction.getAmount());
            BigDecimal newBalance=data.getType().equals(Transaction.Type.INCOME)?oldBalance.add(data.getAmount()):oldBalance.subtract(data.getAmount());
            account.setBalance(newBalance);
            //updating account in db
            accountDao.updateAccount(account);

            //updating transaction
            currentTransaction.setType(data.getType());
            currentTransaction.setAmount(data.getAmount());
            currentTransaction.setCategory(data.getCategory());
            currentTransaction.setDescription(data.getDescription());
            currentTransaction.setTime(data.getTime());

            if(data.isRecurring() && data.getRecurringInterval()!=null && !currentTransaction.getRecurringInterval().equals(data.getRecurringInterval()) ){
                currentTransaction.setRecurringInterval(data.getRecurringInterval());
                currentTransaction.setNextRecurringDate(calculateNextDate(data.getRecurringInterval()));
            }

            if(data.isRecurring()){
                currentTransaction.setRecurring(true);
                currentTransaction.setStatus(Transaction.Status.PENDING);
            }
            else {
                currentTransaction.setStatus(Transaction.Status.COMPLETED);
                currentTransaction.setRecurringInterval(null);
                currentTransaction.setNextRecurringDate(null);
                currentTransaction.setRecurring(false);
            }


            LocalDateTime nextRecurringDate=data.isRecurring() && data.getRecurringInterval()!=null ? calculateNextDate(data.getRecurringInterval()) :null;
            currentTransaction.setNextRecurringDate(nextRecurringDate);

            return transactionDao.updateTransaction(currentTransaction);

        }
        catch (DataAccessException exception){
            logger.error(exception.getMessage());
            throw new DatabaseException("Error occurred while updating transaction");
        }
    }

    @Override
    @Transactional
    public Transaction fetchTransactionByIdAndUserId(int transactionId) {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        try {
            User loggedInUser=userService.findUserName(authentication.getName());
            return transactionDao.fetchTransactionByIdAndUserId(transactionId,loggedInUser.getId());
        }
        catch (DataAccessException exception){
            logger.error(exception.getMessage());
            throw new DatabaseException("Error fetching transaction");
        }

    }

    @Override
    public List<Transaction> fetchUserTransactions() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser=userService.findUserName(authentication.getName());
        try{
            return transactionDao.fetchUserTransactions(loggedInUser.getId());

        }
        catch (DataAccessException exception){
            logger.error("error fetching transactions for logged in user: {}", loggedInUser);
            throw new DatabaseException("Error fetching transactions");
        }
    }

    @Override
    public List<Transaction> getDueTransactions(Transaction.RecurringInterval interval, LocalDateTime today) {
        try{
            return transactionDao.fetchTransactionsDue(interval,today);
        }
        catch(DataAccessException exception){
            System.out.println(exception);
            logger.error("Error in getting due transactions for {} recurring type at {}",interval,today);
            throw new DatabaseException("Error while fetching transactions dues");
        }
    }

    @Override
    public void recurringTransactionUpdate(Transaction transaction) {
        try{
            transactionDao.recurringTransactionUpdate(transaction);
        }
        catch (DataAccessException exception){
            logger.error("Error updating recurring transaction for ID {}",transaction.getId());
            throw  new DatabaseException("Exception occurred while updating recurring transaction");
        }
    }

    @Override
    public List<Transaction> fetchMonthlyTransactions() {
        try{

            LocalDate startDate=LocalDate.now().minusMonths(1).withDayOfMonth(1);
            LocalDate endDate=LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());

            return transactionDao.fetchMonthlyTransactions(startDate,endDate);
        }
        catch (DataAccessException exception){
            logger.error("Error while fetching monthly transactions ",exception);
            throw new DatabaseException("Exception occurred while fetching monthly transactions ");
        }
    }
}