package com.ledgerly.app.expense_tracker.dao.budget;

import com.ledgerly.app.expense_tracker.entity.Budget;
import com.ledgerly.app.expense_tracker.entity.User;

import java.math.BigDecimal;

public interface BudgetDao {

    Budget getCurrentBudget(int userId);
    Budget createBudget(User user, BigDecimal amount);
    Budget updateBudget(BigDecimal amount,User user);
}
