package com.ledgerly.app.expense_tracker.DTO;


import com.ledgerly.app.expense_tracker.entity.Budget;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ExpenseBudgetDTO {

    Budget budget;
    BigDecimal expense;
}
