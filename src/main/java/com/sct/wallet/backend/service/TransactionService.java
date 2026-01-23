package com.sct.wallet.backend.service;

import com.sct.wallet.backend.dto.TransactionRequestDto;
import com.sct.wallet.backend.dto.TransactionResponseDto;
import com.sct.wallet.backend.entity.Transaction;

public interface TransactionService {
    TransactionResponseDto transfer(TransactionRequestDto request);
}
