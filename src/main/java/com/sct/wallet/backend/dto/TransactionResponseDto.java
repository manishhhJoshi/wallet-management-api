package com.sct.wallet.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransactionResponseDto {

    private String reference;
    private BigDecimal amount;
    private String status;

    private UUID debitTransactionId;
    private UUID debitWalletId;
    private LocalDateTime debitCreatedAt;

    private UUID creditTransactionId;
    private UUID creditWalletId;
    private LocalDateTime creditCreatedAt;
}
