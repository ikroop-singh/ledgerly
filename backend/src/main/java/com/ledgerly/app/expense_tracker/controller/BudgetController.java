package com.ledgerly.app.expense_tracker.controller;


import com.ledgerly.app.expense_tracker.DTO.BudgetAddUpdateRequest;
import com.ledgerly.app.expense_tracker.DTO.ExpenseBudgetDTO;
import com.ledgerly.app.expense_tracker.entity.Budget;
import com.ledgerly.app.expense_tracker.service.budget.BudgetService;
import com.ledgerly.app.expense_tracker.service.expenseBudget.ExpenseBudgetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/budget")
public class BudgetController {

    ExpenseBudgetService expenseBudgetService;
    BudgetService budgetService;

    @GetMapping("/account/{accountId}")
    ResponseEntity<ExpenseBudgetDTO> getBudget(@PathVariable int accountId){
        ExpenseBudgetDTO res= expenseBudgetService.getCurrentBudget(accountId);
        return ResponseEntity.status(200).body(res);
    }

    @PostMapping("/add")
    ResponseEntity<Budget>createBudget(@RequestBody BudgetAddUpdateRequest amount){
        Budget budget=budgetService.createBudget(amount.getAmount());
        return ResponseEntity.status(201).body(budget);
    }
    @PatchMapping("/update")
    ResponseEntity<Budget>updateBudget(@RequestBody BudgetAddUpdateRequest amount){
        Budget budget=budgetService.updateBudget(amount.getAmount());
        return ResponseEntity.status(201).body(budget);
    }
}
