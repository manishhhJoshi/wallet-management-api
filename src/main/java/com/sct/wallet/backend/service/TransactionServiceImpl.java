package com.sct.wallet.backend.service;

import com.sct.wallet.backend.dto.TransactionRequestDto;
import com.sct.wallet.backend.dto.TransactionResponseDto;
import com.sct.wallet.backend.entity.Transaction;
import com.sct.wallet.backend.entity.User;
import com.sct.wallet.backend.entity.Wallet;
import com.sct.wallet.backend.entity.enums.TransactionStatus;
import com.sct.wallet.backend.entity.enums.TransactionType;
import com.sct.wallet.backend.repository.TransactionRepository;
import com.sct.wallet.backend.repository.UserRepository;
import com.sct.wallet.backend.repository.WalletRepository;
import com.sct.wallet.backend.specification.TransactionSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public TransactionResponseDto transfer(TransactionRequestDto request) {

        if (request.getFromWalletId().equals(request.getToWalletId())) {
            throw new RuntimeException("Cannot transfer to same wallet");
        }

        Wallet fromWallet = walletRepository.findById(request.getFromWalletId())
                .orElseThrow(() -> new RuntimeException("From wallet not found"));

        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!fromWallet.getUser().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("Unauthorized access to wallet");
        }

        Wallet toWallet = walletRepository.findById(request.getToWalletId())
                .orElseThrow(() -> new RuntimeException("To wallet not found"));

        BigDecimal amount = request.getAmount();

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 1️⃣ Update balances
        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        String reference = UUID.randomUUID().toString();

        // 2️⃣ DEBIT
        Transaction debit = new Transaction();
        debit.setWallet(fromWallet);
        debit.setType(TransactionType.DEBIT);
        debit.setAmount(amount);
        debit.setStatus(TransactionStatus.SUCCESS);
        debit.setReference(reference);

        // 3️⃣ CREDIT
        Transaction credit = new Transaction();
        credit.setWallet(toWallet);
        credit.setType(TransactionType.CREDIT);
        credit.setAmount(amount);
        credit.setStatus(TransactionStatus.SUCCESS);
        credit.setReference(reference);

        Transaction savedDebit = transactionRepository.save(debit);
        Transaction savedCredit = transactionRepository.save(credit);

        return TransactionResponseDto.builder()
                .reference(reference)
                .amount(amount)
                .status(TransactionStatus.SUCCESS.name())

                .debitTransactionId(savedDebit.getId())
                .debitWalletId(savedDebit.getWallet().getId())
                .debitCreatedAt(savedDebit.getCreatedAt())

                .creditTransactionId(savedCredit.getId())
                .creditWalletId(savedCredit.getWallet().getId())
                .creditCreatedAt(savedCredit.getCreatedAt())
                .build();

    }
    @Override
    @Transactional
    public Page<TransactionResponseDto> getMyTransactionHistoryByTime(
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    ) {
        // 1️⃣ Get logged-in user
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Get user's wallets
        List<Wallet> wallets = walletRepository.findByUser_Id(user.getId());

        if (wallets.isEmpty()) return Page.empty();

        List<UUID> walletIds = wallets.stream().map(Wallet::getId).toList();

        // 3️⃣ Fetch transactions of user wallets (time filtered)
        List<Transaction> userTxs = transactionRepository.findAll(
                Specification.where(TransactionSpecification.walletIn(walletIds))
                        .and(TransactionSpecification.createdBetween(from, to))
        );

        // 4️⃣ Get all references from these transactions
        List<String> references = userTxs.stream()
                .map(Transaction::getReference)
                .distinct()
                .toList();

        if (references.isEmpty()) return Page.empty();

        // 5️⃣ Fetch ALL transactions with these references (both debit + credit)
        List<Transaction> allTxs = transactionRepository.findAll(
                TransactionSpecification.referenceIn(references)
        );

        // 6️⃣ Group by reference
        Map<String, List<Transaction>> grouped = allTxs.stream()
                .collect(Collectors.groupingBy(Transaction::getReference));

        // 7️⃣ Map to DTO
        List<TransactionResponseDto> response = grouped.values().stream()
                .map(this::buildTransactionResponse)
                .sorted((a, b) -> b.getCreditCreatedAt().compareTo(a.getCreditCreatedAt())) // latest first
                .collect(Collectors.toList());

        // 8️⃣ Pageable
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), response.size());
        List<TransactionResponseDto> pageContent = response.subList(start, end);

        return new PageImpl<>(pageContent, pageable, response.size());
    }

    // Private Helper
    private TransactionResponseDto buildTransactionResponse(List<Transaction> transactions) {
        TransactionResponseDto.TransactionResponseDtoBuilder builder = TransactionResponseDto.builder();

        for (Transaction tx : transactions) {
            builder.reference(tx.getReference())
                    .amount(tx.getAmount())
                    .status(tx.getStatus().name());

            if (tx.getType() == TransactionType.DEBIT) {
                builder.debitTransactionId(tx.getId())
                        .debitWalletId(tx.getWallet().getId())
                        .debitCreatedAt(tx.getCreatedAt());
            }
            if (tx.getType() == TransactionType.CREDIT) {
                builder.creditTransactionId(tx.getId())
                        .creditWalletId(tx.getWallet().getId())
                        .creditCreatedAt(tx.getCreatedAt());
            }
        }

        return builder.build();
    }

}
