package com.resumebuilder.service;

import com.resumebuilder.dto.*;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.UserRepository;
import com.resumebuilder.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userService.mapToDTO(user))
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userService.mapToDTO(user))
                .build();
    }
}