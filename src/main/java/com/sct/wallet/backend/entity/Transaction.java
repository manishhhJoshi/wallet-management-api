package com.sct.wallet.backend.entity;

import com.sct.wallet.backend.entity.base.BaseAudit;
import com.sct.wallet.backend.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.sct.wallet.backend.entity.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseAudit {

    @Id
    @GeneratedValue
    private UUID id; // Primary key, unique identifier for the transaction

    @ManyToOne
    @JoinColumn(name = "from_wallet_id", nullable = false)
    private Wallet fromWallet; // The wallet that is sending money (debited)

    @ManyToOne
    @JoinColumn(name = "to_wallet_id", nullable = false)
    private Wallet toWallet; // The wallet that is receiving money (credited)

    @Enumerated(EnumType.STRING)
    private TransactionType type; // Transaction type: DEBIT, CREDIT, TRANSFER, etc.

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount; // Amount transferred

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(unique = true, nullable = false)
    private String reference; // Unique reference for tracking
}