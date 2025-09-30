package com.shop.onlineshop.controller;

import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.exception.ApiResponse;
import com.shop.onlineshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Người dùng", description = "Quản lý thông tin người dùng")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🌐 Public (ai cũng xem được user profile cơ bản)
    @Operation(summary = "Xem thông tin user theo ID")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User found", userService.getUserById(id)));
    }

    // 🔒 ADMIN mới xem được toàn bộ user
    @Operation(summary = "Danh sách toàn bộ user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("User list", userService.getAllUsers()));
    }

    // 🔒 USER hoặc ADMIN xem profile của chính mình
    @Operation(summary = "Xem profile của chính mình")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success("My profile", userService.getCurrentUser()));
    }
}
