package com.sct.wallet.backend.service;

import com.sct.wallet.backend.dto.TransactionRequestDto;
import com.sct.wallet.backend.dto.TransactionResponseDto;
import com.sct.wallet.backend.entity.Transaction;
import com.sct.wallet.backend.entity.Wallet;
import com.sct.wallet.backend.entity.enums.TransactionStatus;
import com.sct.wallet.backend.repository.TransactionRepository;
import com.sct.wallet.backend.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public TransactionResponseDto transfer(TransactionRequestDto request) {

        if (request.getFromWalletId().equals(request.getToWalletId())) {
            throw new RuntimeException("Cannot transfer to the same wallet");
        }

        Wallet fromWallet = walletRepository.findById(request.getFromWalletId())
                .orElseThrow(() -> new RuntimeException("From Wallet not found"));

        Wallet toWallet = walletRepository.findById(request.getToWalletId())
                .orElseThrow(() -> new RuntimeException("To Wallet not found"));

        BigDecimal amount = request.getAmount();

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Update balances
        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setFromWallet(fromWallet);
        transaction.setToWallet(toWallet);
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setReference(UUID.randomUUID().toString());

        Transaction saved = transactionRepository.save(transaction);

        // Entity → DTO
        return TransactionResponseDto.builder()
                .transactionId(saved.getId())
                .fromWalletId(fromWallet.getId())
                .toWalletId(toWallet.getId())
                .amount(saved.getAmount())
                .status(saved.getStatus().name())
                .reference(saved.getReference())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
