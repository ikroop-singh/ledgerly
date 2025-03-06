package com.ledgerly.app.expense_tracker.DTO;

import java.math.BigDecimal;

public class BudgetAddUpdateRequest {
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}