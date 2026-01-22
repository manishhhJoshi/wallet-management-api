package com.sct.wallet.backend.service;

import com.sct.wallet.backend.dto.SignUpRequestDto;
import com.sct.wallet.backend.entity.Role;
import com.sct.wallet.backend.entity.User;
import com.sct.wallet.backend.entity.Wallet;
import com.sct.wallet.backend.entity.enums.UserStatus;
import com.sct.wallet.backend.entity.enums.WalletStatus;
import com.sct.wallet.backend.exception.ResourceNotFoundException;
import com.sct.wallet.backend.repository.RoleRepository;
import com.sct.wallet.backend.repository.UserRepository;
import com.sct.wallet.backend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final WalletRepository walletRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(SignUpRequestDto signUpRequestDto) {

        if(userRepo.existsByUsername(signUpRequestDto.getUsername())){
            throw new RuntimeException("User Already Exist By "+signUpRequestDto.getUsername()+" Name");
        }

        if(userRepo.existsByEmail(signUpRequestDto.getEmail())){
            throw new RuntimeException("User By this email"+signUpRequestDto.getEmail()+" already exist");
        }

        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_USER not found"));


        User user = new User();
        user.setUsername(signUpRequestDto.getUsername());
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(Set.of(userRole));

        userRepo.save(user);


        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("NPR");

        walletRepo.save(wallet);

    }
}
