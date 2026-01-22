package com.sct.wallet.backend.controller;

import com.sct.wallet.backend.config.JwtTokenProvider;
import com.sct.wallet.backend.dto.LoginRequestDto;
import com.sct.wallet.backend.dto.SignUpRequestDto;
import com.sct.wallet.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        // 1. Authenticate username + password
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        // 2. Generate JWT
        String token = tokenProvider.generateToken(authentication);

        // 3. Return token
        return ResponseEntity.ok(
                Map.of("token", token)
        );
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto dto){
        authService.registerUser(dto);
        return ResponseEntity.ok("Sign Up Successful");
    }
}
