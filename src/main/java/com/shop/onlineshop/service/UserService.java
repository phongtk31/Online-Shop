package com.shop.onlineshop.service;

import com.shop.onlineshop.dto.UserDTO;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDTO createUser(String username, String password, Set<String> roles);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);          // 👈 thêm
    UserDTO getCurrentUser();
}
