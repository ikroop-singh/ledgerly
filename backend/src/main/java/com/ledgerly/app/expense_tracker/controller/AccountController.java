package com.ledgerly.app.expense_tracker.controller;

import com.ledgerly.app.expense_tracker.entity.Account;
import com.ledgerly.app.expense_tracker.service.account.AccountService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
@CrossOrigin("*")
public class AccountController {

    AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account){

        return accountService.createAccount(account);
    }

    @GetMapping("/user-accounts")
    public List<Account> getUserAccounts(){
        return accountService.getUserAccounts();
    }

    @PutMapping("/set-default/{accountId}")
    public ResponseEntity<Account> updateDefaultAccount(@PathVariable int accountId){
        return accountService.setDefaultAccount(accountId);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable int accountId){
        Account account= accountService.findAccountById(accountId);
        return ResponseEntity.status(200).body(account);
    }
}
