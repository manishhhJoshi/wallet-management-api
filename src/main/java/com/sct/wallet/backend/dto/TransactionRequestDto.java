package com.sct.wallet.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequestDto {
    private UUID fromWalletId;
    private UUID toWalletId;
    private BigDecimal amount;

}
