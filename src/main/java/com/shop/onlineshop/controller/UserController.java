package com.shop.onlineshop.controller;

import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.exception.ApiResponse;
import com.shop.onlineshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> create(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        @SuppressWarnings("unchecked")
        Set<String> roles = Set.copyOf((List<String>) body.get("roles"));

        UserDTO user = userService.createUser(username, password, roles);
        return ResponseEntity.ok(ApiResponse.success("User created", user));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("User list", userService.getAllUsers()));
    }
}
