package com.sct.wallet.backend.service;

import com.sct.wallet.backend.dto.TransactionRequestDto;
import com.sct.wallet.backend.dto.TransactionResponseDto;
import com.sct.wallet.backend.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TransactionService {
    TransactionResponseDto transfer(TransactionRequestDto request);
    Page<TransactionResponseDto> getMyTransactionHistoryByTime(LocalDateTime from, LocalDateTime to, Pageable pageable
    );

}
