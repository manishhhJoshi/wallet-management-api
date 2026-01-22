package com.sct.wallet.backend.dto;

import com.sct.wallet.backend.entity.Transaction;
import com.sct.wallet.backend.entity.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class TransactionResponseDto {

    private UUID transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private TransactionStatus status;
    private String reference;
    private LocalDateTime createdAt;

    public static TransactionResponseDto from(Transaction tx){
        return TransactionResponseDto.builder()
                .transactionId(tx.getId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .reference(tx.getReference())
                .createdAt(tx.getCreatedAt())
                .build();

    }
}
