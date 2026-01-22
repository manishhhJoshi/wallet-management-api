package com.sct.wallet.backend.config;

import com.sct.wallet.backend.entity.User;
import com.sct.wallet.backend.entity.enums.UserStatus;
import com.sct.wallet.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        CustomUserDetails customUserDetails = new CustomUserDetails(user, authorities);
        System.out.println("Authorities from CustomUserDetailsService: " + customUserDetails.getAuthorities());
        return customUserDetails;

    }

}
