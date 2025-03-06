package com.ledgerly.app.expense_tracker.service.budget;


import com.ledgerly.app.expense_tracker.entity.Budget;

import java.math.BigDecimal;

public interface BudgetService {
    Budget createBudget(BigDecimal amount);
    Budget updateBudget(BigDecimal amount);
}
