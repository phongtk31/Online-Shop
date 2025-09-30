package com.shop.onlineshop.service;

import com.shop.onlineshop.config.JwtUtil;
import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.dto.auth.AuthResponse;
import com.shop.onlineshop.dto.auth.LoginRequest;
import com.shop.onlineshop.dto.auth.RefreshRequest;
import com.shop.onlineshop.dto.auth.RegisterRequest;
import com.shop.onlineshop.entity.RoleEntity;
import com.shop.onlineshop.entity.UserEntity;
import com.shop.onlineshop.repository.RoleRepository;
import com.shop.onlineshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserDTO register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        RoleEntity roleUser = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> roleRepo.save(new RoleEntity(null, "ROLE_USER")));

        UserEntity u = UserEntity.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .enabled(true)
                .roles(Set.of(roleUser))
                .build();

        UserEntity saved = userRepo.save(u);
        return toDTO(saved);
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        User principal = (User) auth.getPrincipal();
        String access = jwtUtil.generateAccessToken(principal);
        String refresh = jwtUtil.generateRefreshToken(principal);
        return new AuthResponse(access, refresh, "Bearer");
    }

    @Override
    public AuthResponse refresh(RefreshRequest req) {
        String username = jwtUtil.extractUsername(req.getRefreshToken());
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        User springUser = new User(
                user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true,
                user.getRoles().stream()
                        .map(r -> new org.springframework.security.core.authority.SimpleGrantedAuthority(r.getName()))
                        .collect(Collectors.toSet())
        );

        // validate refresh token
        var claims = jwtUtil.extractAllClaims(req.getRefreshToken());
        if (!"refresh".equals(claims.get("type"))) {
            throw new IllegalArgumentException("Invalid token type");
        }
        if (!jwtUtil.isTokenValid(req.getRefreshToken(), springUser)) {
            throw new IllegalArgumentException("Refresh token invalid or expired");
        }

        String newAccess = jwtUtil.generateAccessToken(springUser);
        return new AuthResponse(newAccess, req.getRefreshToken(), "Bearer");
    }

    private UserDTO toDTO(UserEntity u) {
        return UserDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .enabled(u.isEnabled())
                .roles(u.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()))
                .build();
    }
}
