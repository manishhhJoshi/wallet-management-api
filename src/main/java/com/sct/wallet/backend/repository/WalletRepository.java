package com.sct.wallet.backend.repository;

import com.sct.wallet.backend.dto.WalletResponseDto;
import com.sct.wallet.backend.entity.User;
import com.sct.wallet.backend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByUser(User user);
}
