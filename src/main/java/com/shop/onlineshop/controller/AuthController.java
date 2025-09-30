package com.shop.onlineshop.controller;

import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.dto.auth.*;
import com.shop.onlineshop.exception.ApiResponse;
import com.shop.onlineshop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Xác thực", description = "Đăng nhập, đăng ký, refresh token")
public class AuthController {

    private final AuthService authService;

    // Public register (ROLE_USER mặc định)
    @Operation(summary = "Đăng ký", description = "Tạo tài khoản mới")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest req) {
        UserDTO user = authService.register(req);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", user));
    }

    // Login -> cấp access + refresh
    @Operation(summary = "Đăng nhập", description = "Login với username và password")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest req) {
        AuthResponse tokens = authService.login(req);
        return ResponseEntity.ok(ApiResponse.success("Login success", tokens));
    }

    // 🌐 Public
    @Operation(summary = "Làm mới Access Token bằng Refresh Token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest req) {
        AuthResponse tokens = authService.refresh(req);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", tokens));
    }
}
