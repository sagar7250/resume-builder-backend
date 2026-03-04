package com.resumebuilder.controller;

import com.resumebuilder.dto.*;
import com.resumebuilder.entity.User;
import com.resumebuilder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserProfile(user.getId()), "Profile fetched"));
    }

    @PutMapping("/me")
    @Operation(summary = "Update profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UserDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateProfile(user.getId(), dto), "Profile updated"));
    }
}