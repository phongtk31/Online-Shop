package com.shop.onlineshop.dto.auth;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}
