package com.ledgerly.app.expense_tracker.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private Type type;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="is_default")
    @JsonProperty("isDefault")
    private boolean isDefault=false;

    @ManyToOne()
    @JoinColumn(name="user")
    private User user;

//    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY)
//    List<Transaction>transactions;

    @Column(insertable = false)
    private LocalDateTime created_at;

    @Column(updatable = false)
    private LocalDateTime updated_at;

    public enum Type{
        CURRENT,SAVINGS
    }
}
