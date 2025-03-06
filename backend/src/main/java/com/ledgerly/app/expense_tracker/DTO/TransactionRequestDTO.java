package com.ledgerly.app.expense_tracker.DTO;

import com.ledgerly.app.expense_tracker.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TransactionRequestDTO {
    private BigDecimal amount;
    private String description;
    private Transaction.Type type;
    private LocalDate time;
    private String category;
    private String receiptUrl;
    private boolean isRecurring = false;
    private Transaction.RecurringInterval recurringInterval;
    private LocalDateTime nextRecurringDate;
    private LocalDateTime lastProcessed;
    private Transaction.Status status = Transaction.Status.COMPLETED;
    private Integer accountId;
}
