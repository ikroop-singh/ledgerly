package com.ledgerly.app.expense_tracker.dao.budget;


import com.ledgerly.app.expense_tracker.entity.Budget;
import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.exceptions.BudgetNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@AllArgsConstructor
public class BudgetDaoImpl implements BudgetDao{

    EntityManager entityManager;

    @Override
    public Budget getCurrentBudget(int userId) {
        Budget budget=null;
        try{
            TypedQuery<Budget>query=entityManager.createQuery(" from Budget where user.id = :userId", Budget.class);
            query.setParameter("userId",userId);
            budget= query.getSingleResult();
        }
        catch(Exception exc){
            System.out.println(exc);
        }
        return budget;
    }

    @Override
    @Transactional
    public Budget createBudget(User user, BigDecimal amount) {
        Budget budget=new Budget();
        budget.setAmount(amount);
        budget.setUser(user);
        entityManager.persist(budget);
        return budget;
    }

    @Override
    @Transactional
    public Budget updateBudget(BigDecimal amount, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null when updating budget");
        }

        Budget budget;
        try {
            TypedQuery<Budget> findBudget = entityManager.createQuery(
                    "from Budget where user = :user", Budget.class);
            findBudget.setParameter("user", user);
            budget = findBudget.getSingleResult(); // Fetch the existing budget
        } catch (NoResultException exc) {
            throw new BudgetNotFoundException("Budget not found");
        }

        // Update the budget amount
        Query query = entityManager.createQuery(
                "update Budget set amount = :amount where user = :user");
        query.setParameter("amount", amount);
        query.setParameter("user", user);
        query.executeUpdate();

        // Refresh and return the updated budget
        entityManager.refresh(budget); // Ensures we get the latest DB state
        return budget;
    }
}
