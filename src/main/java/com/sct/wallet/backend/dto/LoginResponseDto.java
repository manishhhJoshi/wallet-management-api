package com.sct.wallet.backend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponseDto {

    private String token;
    private String tokenType = "Bearer";
}
