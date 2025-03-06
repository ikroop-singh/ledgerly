package com.ledgerly.app.expense_tracker.dao.transaction;

import com.ledgerly.app.expense_tracker.entity.Account;
import com.ledgerly.app.expense_tracker.entity.Transaction;
import com.ledgerly.app.expense_tracker.exceptions.AccountNotFoundException;
import com.ledgerly.app.expense_tracker.exceptions.DatabaseException;
import com.ledgerly.app.expense_tracker.exceptions.DatabaseExceptionHandler;
import com.ledgerly.app.expense_tracker.exceptions.TransactionNotFoundException;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
@AllArgsConstructor
public class TransactionDaoImpl implements TransactionDao{

    EntityManager entityManager;
    private static final Logger logger= LoggerFactory.getLogger(TransactionDaoImpl.class);


    @Override
    @Transactional
    public void deleteTransactions(List<Integer> ids) {
       long count=entityManager.createQuery("select count(t.id) from Transaction t where t.id in :ids",Long.class)
               .setParameter("ids",ids)
               .getSingleResult();

       if(count!=ids.size()){
           throw new TransactionNotFoundException("Transaction not found for some ids");
       }
        //update account balance
        for (int id : ids) {
            Transaction transaction = entityManager.find(Transaction.class,id);
            Account account=transaction.getAccount();
           BigDecimal balance= account.getBalance();
           BigDecimal updatedBalance=transaction.getType().equals("INCOME")?balance.subtract(transaction.getAmount()):balance.add(transaction.getAmount());
            account.setBalance(updatedBalance);
            entityManager.merge(account);

        }
       //delete query to delete transactions
        Query query= entityManager.createQuery("delete from Transaction t where t.id in :ids");
        query.setParameter("ids",ids);
        query.executeUpdate();
    }

    @Override
    public List<Transaction> fetchTransactionsByAccountId(int accountId) {
        TypedQuery<Transaction> query=entityManager.createQuery("from Transaction where account.id = :accountId",Transaction.class);
        query.setParameter("accountId",accountId);
        return query.getResultList();
    }

    @Override
    public BigDecimal getTotalExpense(int accountId, int userId,LocalDate startDate, LocalDate endDate) {

            TypedQuery<BigDecimal> query = entityManager.createQuery("select sum(amount) from Transaction where user.id= :userId and account.id= :accountId and type='EXPENSE' and time >= :startDate and time <= :endDate ", BigDecimal.class);
            query.setParameter("accountId", accountId);
            query.setParameter("userId", userId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
           return query.getSingleResult();
    }

    @Override
    public void createTransaction(Transaction data) {
        entityManager.persist(data);
    }

    @Override
    public Transaction fetchTransactionByIdAndUserId(int transactionId, int userId) {
        TypedQuery<Transaction> query=entityManager.createQuery("from Transaction t where user.id =:userId and t.id=:transactionId",Transaction.class);
        query.setParameter("transactionId",transactionId);
        query.setParameter("userId",userId);
        return query.getSingleResult();
    }

    @Override
    public Transaction updateTransaction(Transaction transactionData) {
       return entityManager.merge(transactionData);
    }

    @Override
    public List<Transaction> fetchUserTransactions(int userId) {
        TypedQuery<Transaction> query=entityManager.createQuery("from Transaction where user.id=:userId",Transaction.class);
        query.setParameter("userId",userId);
        return query.getResultList();
    }

    @Override
    public List<Transaction> fetchTransactionsDue(Transaction.RecurringInterval interval, LocalDateTime today) {
        TypedQuery<Transaction>query=entityManager.createQuery("from Transaction where nextRecurringDate >=:today and recurringInterval =:interval",Transaction.class);
        query.setParameter("interval",interval);
        query.setParameter("today",today);
        return query.getResultList();

    }

    @Override
    public void recurringTransactionUpdate(Transaction transaction) {
        entityManager.merge(transaction);
    }

    @Override
    public List<Transaction> fetchMonthlyTransactions( LocalDate startDate, LocalDate endDate) {
        TypedQuery<Transaction> query=entityManager.createQuery("from Transaction where time>=:startDate and time<=:endDate",Transaction.class);
        query.setParameter("startDate",startDate);
        query.setParameter("endDate",endDate);
        return query.getResultList();
    }
}
