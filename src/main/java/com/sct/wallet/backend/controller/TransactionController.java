package com.sct.wallet.backend.controller;

import com.sct.wallet.backend.dto.TransactionRequestDto;
import com.sct.wallet.backend.dto.TransactionResponseDto;
import com.sct.wallet.backend.entity.Transaction;
import com.sct.wallet.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transferMoney(
            @RequestBody TransactionRequestDto request) {

        return ResponseEntity.ok(transactionService.transfer(request));
    }
}
