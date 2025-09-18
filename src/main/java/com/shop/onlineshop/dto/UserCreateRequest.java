package com.shop.onlineshop.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private Set<String> roles; // e.g. ["ROLE_USER","ROLE_ADMIN"]
}
