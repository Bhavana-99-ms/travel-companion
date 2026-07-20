package com.travel.service;

import com.travel.dto.RegisterDto;
import com.travel.enums.Role;
import com.travel.model.User;
import com.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User register(RegisterDto dto) {
        Role role = "PROVIDER".equalsIgnoreCase(dto.getRole()) ? Role.ROLE_PROVIDER : Role.ROLE_USER;

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .city(dto.getCity())
                .role(role)
                .enabled(true)
                .build();

        return userRepository.save(user);
    }
}
