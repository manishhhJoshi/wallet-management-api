package com.sct.wallet.backend.controller;

import com.sct.wallet.backend.config.CustomUserDetails;
import com.sct.wallet.backend.dto.WalletResponseDto;
import com.sct.wallet.backend.entity.User;
import com.sct.wallet.backend.entity.Wallet;
import com.sct.wallet.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService service;

    @GetMapping("/me")
    public WalletResponseDto getMyWallet() {
        return service.getMyWallet();
    }

}
