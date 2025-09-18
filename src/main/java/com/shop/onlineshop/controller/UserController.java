package com.shop.onlineshop.controller;

import com.shop.onlineshop.dto.UserCreateRequest;
import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.exception.ApiResponse;
import com.shop.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Admin tạo user (có thể chỉ định roles)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> create(@RequestBody UserCreateRequest req) {
        UserDTO user = userService.createUser(req.getUsername(), req.getPassword(), req.getRoles());
        return ResponseEntity.ok(ApiResponse.success("User created", user));
    }

    // Danh sách user (yêu cầu đã đăng nhập)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("User list", userService.getAllUsers()));
    }
}
