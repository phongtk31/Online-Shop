package com.shop.onlineshop.controller;

import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.dto.auth.*;
import com.shop.onlineshop.exception.ApiResponse;
import com.shop.onlineshop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Public register (ROLE_USER mặc định)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest req) {
        UserDTO user = authService.register(req);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", user));
    }

    // Login -> cấp access + refresh
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest req) {
        AuthResponse tokens = authService.login(req);
        return ResponseEntity.ok(ApiResponse.success("Login success", tokens));
    }

    // Refresh access token
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest req) {
        AuthResponse tokens = authService.refresh(req);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", tokens));
    }
}
