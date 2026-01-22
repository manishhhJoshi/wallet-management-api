package com.sct.wallet.backend.dto;

import com.sct.wallet.backend.entity.enums.WalletStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class WalletResponseDto {
    private UUID id;
    private BigDecimal balance;
    private String currency;
    private WalletStatus status;
}
