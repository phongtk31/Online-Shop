package com.shop.onlineshop.controller;

import com.shop.onlineshop.config.JwtUtil;
import com.shop.onlineshop.exception.ApiResponse;
import com.shop.onlineshop.service.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil,
                          CustomUserDetailsService customUserDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                var roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                String token = jwtUtil.generateToken(username, roles);
                return ResponseEntity.ok(ApiResponse.success("Login success", token));
            } else {
                return ResponseEntity.status(401).body(ApiResponse.error("Invalid credentials"));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body(ApiResponse.error("User not found"));
        }
    }
}
