package com.luxcrm.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luxcrm.config.JwtTokenProvider;
import com.luxcrm.auth.dto.*;
import com.luxcrm.entities.User;
import com.luxcrm.enums.Role;
import com.luxcrm.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.SALES)
                .status("active")
                .build();

        userRepository.save(user);
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token);
    }
}
