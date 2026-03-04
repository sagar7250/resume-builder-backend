package com.resumebuilder.dto;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private UserDTO user;
}
