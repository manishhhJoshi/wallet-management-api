package com.sct.wallet.backend.service;

import com.sct.wallet.backend.config.CustomUserDetails;
import com.sct.wallet.backend.dto.WalletResponseDto;
import com.sct.wallet.backend.entity.User;


public interface WalletService {
    WalletResponseDto getMyWallet();
}
