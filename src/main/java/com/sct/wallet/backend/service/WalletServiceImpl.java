package com.sct.wallet.backend.service;

import com.sct.wallet.backend.config.CustomUserDetails;
import com.sct.wallet.backend.dto.WalletResponseDto;
import com.sct.wallet.backend.entity.User;
import com.sct.wallet.backend.entity.Wallet;
import com.sct.wallet.backend.exception.ResourceNotFoundException;
import com.sct.wallet.backend.repository.UserRepository;
import com.sct.wallet.backend.repository.WalletRepository;
import com.sct.wallet.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepo;

    @Override
    public WalletResponseDto getMyWallet() {
        User currentUser = SecurityUtils.getCurrentUser();

        Wallet wallet = walletRepo.findByUser(currentUser)
                .orElseThrow(()-> new ResourceNotFoundException("Wallet not found for user"));

        return mapToDto(wallet);
    }

    private WalletResponseDto mapToDto(Wallet wallet){
        return WalletResponseDto.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .status(wallet.getStatus())
                .build();
    }
}
