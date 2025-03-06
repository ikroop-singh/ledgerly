package com.ledgerly.app.expense_tracker.service.account;
import com.ledgerly.app.expense_tracker.dao.account.AccountDao;
import com.ledgerly.app.expense_tracker.entity.Account;
import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.service.user.UserService;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class AccountServiceImpl implements AccountService {

    AccountDao accountDao;
    UserService userService;

    @Override
    public ResponseEntity<Account> createAccount(Account account) {
        //Get logged-in user
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Object principal=authentication.getPrincipal();// This returns User which is a type of spring security User
        User loggedInUser=userService.findUserName(authentication.getName());
        account.setUser(loggedInUser);

        //Check for the existing accounts
        List<Account> existingAccounts=accountDao.getUserAccounts(loggedInUser);
        Boolean shouldBeDefault=existingAccounts.size()==0?true:account.isDefault();

        if(shouldBeDefault){
            //updating all the accounts
            accountDao.unsetDefaultAccount(loggedInUser);
            account.setDefault(true);
        }
        return accountDao.createAccount(account);
    }
    public List<Account> getUserAccounts(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser=userService.findUserName(authentication.getName());
        return accountDao.getUserAccounts(loggedInUser);
    }

    @Override
    public ResponseEntity<Account> setDefaultAccount(int accountId) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser=userService.findUserName(authentication.getName());
        return accountDao.setDefaultAccount(loggedInUser,accountId);

    }

    @Override
    public Account findAccountById(int accountId) {
        return accountDao.findAccountById(accountId);
    }
}