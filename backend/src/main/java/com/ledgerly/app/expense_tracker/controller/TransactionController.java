package com.ledgerly.app.expense_tracker.controller;


import com.ledgerly.app.expense_tracker.DTO.TransactionRequestDTO;
import com.ledgerly.app.expense_tracker.entity.Transaction;
import com.ledgerly.app.expense_tracker.service.aiScanner.AIScannerService;
import com.ledgerly.app.expense_tracker.service.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    TransactionService transactionService;
    AIScannerService aiScannerService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTransactions(@RequestBody List<Integer> ids){
        transactionService.deleteTransactions(ids);
        return ResponseEntity.status(200).body("Transactions deleted successfully");
    }
    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> fetchTransactionsByAccountId(@PathVariable int accountId){
        List<Transaction> transaction=transactionService.fetchTransactionsByAccountId(accountId);
        return ResponseEntity.status(200).body(transaction);
    }

    @PostMapping("/add")
    public ResponseEntity<String> createTransaction(@RequestBody TransactionRequestDTO data){
        transactionService.createTransaction(data);
        return ResponseEntity.status(201).body("Transaction created successfully");
    }

    @PatchMapping("/update/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody TransactionRequestDTO data, @PathVariable int transactionId){
        Transaction transaction=transactionService.updateTransaction(data, transactionId);
        return ResponseEntity.status(200).body(transaction);
    }

    @GetMapping("/current-user")
    ResponseEntity<List<Transaction>> fetchUserTransactions(){
        List<Transaction>transactions= transactionService.fetchUserTransactions();
        return ResponseEntity.status(200).body(transactions);
    }

    @GetMapping("/fetch/{transactionId}")
    public ResponseEntity<Transaction> fetchTransactionByIdAndUserId(@PathVariable int transactionId){

        Transaction transaction=transactionService.fetchTransactionByIdAndUserId(transactionId);
        return ResponseEntity.status(200).body(transaction);
    }

    @PostMapping("/scan-receipt")
    public ResponseEntity<String> scanReceipt(@RequestParam("file") MultipartFile file){
        if(file.isEmpty())
            return ResponseEntity.badRequest().body("File is empty");

        String res= aiScannerService.scanReceipt(file);
        return ResponseEntity.status(200).body(res);
    }
}
