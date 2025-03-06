package com.ledgerly.app.expense_tracker.service.account;

import com.ledgerly.app.expense_tracker.entity.Account;
import com.ledgerly.app.expense_tracker.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {

    ResponseEntity<Account> createAccount(Account account);
    List< Account> getUserAccounts();
    ResponseEntity<Account> setDefaultAccount(int accountId);
    Account findAccountById(int accountId);
}
