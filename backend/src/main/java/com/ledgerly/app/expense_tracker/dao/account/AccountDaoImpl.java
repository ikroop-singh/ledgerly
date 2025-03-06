package com.ledgerly.app.expense_tracker.dao.account;

import com.ledgerly.app.expense_tracker.entity.Account;
import com.ledgerly.app.expense_tracker.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@AllArgsConstructor
@Repository
public class AccountDaoImpl implements AccountDao{

    EntityManager entityManager;
    @Override
    @Transactional
    public ResponseEntity<Account> createAccount(Account account) {

        Account createdAccount=entityManager.merge(account);
        return ResponseEntity.status(201).body(createdAccount);
    }

    @Override
    public List<Account> getUserAccounts(User loggedInUser) {
        TypedQuery<Account> query=entityManager.createQuery("from Account where user.id=:loggedInUser",Account.class);
        query.setParameter("loggedInUser",loggedInUser.getId());
        return query.getResultList();
    }

    @Override
    @Transactional
    public void unsetDefaultAccount(User user) {
        Query query=entityManager.createQuery("update Account a set a.isDefault=false where a.isDefault=true and a.user=:user");
        query.setParameter("user",user);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public ResponseEntity<Account> setDefaultAccount(User user, int accountId) {
        unsetDefaultAccount(user);
        Account account=entityManager.find(Account.class,accountId);
        account.setDefault(true);
        entityManager.merge(account);
        return ResponseEntity.status(200).body(account);
    }

    @Override
    public Account findAccountById(int accountId) {
        return entityManager.find(Account.class,accountId);
    }

    @Override
    public Account updateAccount(Account account) {
        return entityManager.merge(account);
    }
}