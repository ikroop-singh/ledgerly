package com.ledgerly.app.expense_tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    int id;

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    Type type;

    @Column(name="amount")
    BigDecimal amount;

    @Column(name="description")
    String description;

    @Column(name="date")
    LocalDate time;

    @Column(name="category")
    String category;

    @Column(name = "receipt_url")
    String receiptUrl;

    @Column(name="is_recurring")
    boolean isRecurring=false;

    @Enumerated(EnumType.STRING)
    @Column(name="recurring_interval")
    RecurringInterval recurringInterval;

    @Column(name="next_recurring_date")
    LocalDateTime nextRecurringDate;

    @Column(name="last_processed")
    LocalDateTime lastProcessed;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    Status status=Status.COMPLETED;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user")
    @JsonIgnore
    User user;


    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="account")
    Account account;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDateTime created_at;

    @Column(insertable = false)
    @UpdateTimestamp
    LocalDateTime updated_at;

    public enum RecurringInterval{
        DAILY,WEEKLY,MONTHLY,YEARLY
    }
    public enum Status{
        PENDING,COMPLETED,FAILED
    }
    public enum Type{
        INCOME,EXPENSE
    }
}
