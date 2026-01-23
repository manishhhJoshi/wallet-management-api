package com.sct.wallet.backend.repository;

import com.sct.wallet.backend.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    // Find transactions where this wallet is the source
    Page<Transaction> findByFromWallet_Id(UUID walletId, Pageable pageable);

    // Find transactions where this wallet is the destination
    Page<Transaction> findByToWallet_Id(UUID walletId, Pageable pageable);

}
