package com.ledgerly.app.expense_tracker.service.expenseBudget;

import com.ledgerly.app.expense_tracker.DTO.ExpenseBudgetDTO;
import com.ledgerly.app.expense_tracker.dao.budget.BudgetDao;
import com.ledgerly.app.expense_tracker.dao.transaction.TransactionDao;
import com.ledgerly.app.expense_tracker.entity.Budget;
import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.exceptions.DatabaseException;
import com.ledgerly.app.expense_tracker.service.user.UserService;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ExpenseBudgetServiceImpl implements ExpenseBudgetService{

    UserService userService;
    TransactionDao transactionDao;
    BudgetDao budgetDao;

    private static final Logger logger = LoggerFactory.getLogger(ExpenseBudgetServiceImpl.class);

    @Override
    @Transactional(rollbackFor ={DatabaseException.class})
    public ExpenseBudgetDTO getCurrentBudget(int accountId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser=userService.findUserName(authentication.getName());

        LocalDate startDate= LocalDate.now().withDayOfMonth(1);
        LocalDate endDate =LocalDate.now().withDayOfMonth(startDate.lengthOfMonth());

        try{
            //To get total expense
            BigDecimal totalExpense=transactionDao.getTotalExpense(accountId,loggedInUser.getId(),startDate,endDate);

            //Get budget
            Budget budget = budgetDao.getCurrentBudget(loggedInUser.getId());
            return new ExpenseBudgetDTO(budget,totalExpense);
        }
        catch(DataAccessException exception){
            logger.error("Error in database operation "+exception.getMessage(),exception);
            throw new DatabaseException("Database error occurred "+exception.getMessage());
        }
        catch(Exception exception){
            logger.error("Error in database operation "+exception.getMessage(),exception);
            throw new DatabaseException("Database error occurred "+exception.getMessage());
        }

    }
}
