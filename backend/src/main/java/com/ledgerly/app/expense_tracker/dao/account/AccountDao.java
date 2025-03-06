package com.ledgerly.app.expense_tracker.dao.account;

import com.ledgerly.app.expense_tracker.entity.Account;
import com.ledgerly.app.expense_tracker.entity.Transaction;
import com.ledgerly.app.expense_tracker.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountDao {

    public  ResponseEntity<Account> createAccount(Account account);
    public List<Account> getUserAccounts(User loggedInUser);
    public ResponseEntity<Account> setDefaultAccount(User user,int accountId);
    public void unsetDefaultAccount(User user);
    Account findAccountById(int accountId);
    Account updateAccount(Account account);
}
