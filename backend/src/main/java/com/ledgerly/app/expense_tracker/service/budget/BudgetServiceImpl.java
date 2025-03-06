package com.ledgerly.app.expense_tracker.service.budget;

import com.ledgerly.app.expense_tracker.dao.budget.BudgetDao;
import com.ledgerly.app.expense_tracker.entity.Budget;
import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    BudgetDao budgetDao;
    UserService userService;
    @Override
    public Budget createBudget(BigDecimal amount) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser=userService.findUserName(authentication.getName());

        return budgetDao.createBudget(loggedInUser,amount);
    }

    @Override
    public Budget updateBudget(BigDecimal amount) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser=userService.findUserName(authentication.getName());
        return budgetDao.updateBudget(amount,loggedInUser);
    }

}
