package com.shop.onlineshop.service;

import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.dto.auth.AuthResponse;
import com.shop.onlineshop.dto.auth.LoginRequest;
import com.shop.onlineshop.dto.auth.RefreshRequest;
import com.shop.onlineshop.dto.auth.RegisterRequest;

public interface AuthService {
    UserDTO register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
    AuthResponse refresh(RefreshRequest req);
}
