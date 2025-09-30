package com.shop.onlineshop.service.impl;

import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.entity.RoleEntity;
import com.shop.onlineshop.entity.UserEntity;
import com.shop.onlineshop.exception.ResourceNotFoundException;
import com.shop.onlineshop.repository.RoleRepository;
import com.shop.onlineshop.repository.UserRepository;
import com.shop.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @Override
    public UserDTO createUser(String username, String rawPassword, Set<String> roles) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        Set<RoleEntity> roleEntities = roles.stream()
                .map(name -> roleRepo.findByName(name)
                        .orElseGet(() -> roleRepo.save(new RoleEntity(null, name))))
                .collect(Collectors.toSet());

        UserEntity u = UserEntity.builder()
                .username(username)
                .password(encoder.encode(rawPassword))
                .enabled(true)
                .roles(roleEntities)
                .build();
        return toDTO(userRepo.save(u));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return toDTO(user);
    }

    @Override
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }
        String username = authentication.getName();
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
        return toDTO(user);
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
