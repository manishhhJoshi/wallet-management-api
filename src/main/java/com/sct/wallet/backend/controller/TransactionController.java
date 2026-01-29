package com.sct.wallet.backend.controller;

import com.sct.wallet.backend.dto.TransactionRequestDto;
import com.sct.wallet.backend.dto.TransactionResponseDto;
import com.sct.wallet.backend.entity.Transaction;
import com.sct.wallet.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping("/my/history")
    public ResponseEntity<Page<TransactionResponseDto>> getMyTransactionHistoryByTime(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            Pageable pageable
    ) {
        // Defaults: last 30 days if not provided
        if (from == null) from = LocalDateTime.now().minusDays(30);
        if (to == null) to = LocalDateTime.now();

        return ResponseEntity.ok(
                transactionService.getMyTransactionHistoryByTime(from, to, pageable)
        );
    }

}
