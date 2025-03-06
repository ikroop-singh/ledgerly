package com.ledgerly.app.expense_tracker.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="budget")
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name="amount")
    BigDecimal amount;

    @Column(name="last_alert_sent")
    LocalDateTime lastAlertSent;

    @ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="user")

    User user;

    @Column(insertable = false)
    LocalDateTime createdAt;

    @Column(updatable = false)
    LocalDateTime updatedAt;
}
