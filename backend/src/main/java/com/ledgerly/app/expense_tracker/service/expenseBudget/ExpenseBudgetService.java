package com.ledgerly.app.expense_tracker.service.expenseBudget;

import com.ledgerly.app.expense_tracker.DTO.ExpenseBudgetDTO;

public interface ExpenseBudgetService {

    ExpenseBudgetDTO getCurrentBudget(int accountId);

}
