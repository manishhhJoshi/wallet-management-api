package com.sct.wallet.backend.service;

import com.sct.wallet.backend.dto.SignUpRequestDto;

public interface AuthService {
    void registerUser(SignUpRequestDto signUpRequestDto);
}
