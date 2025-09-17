package com.shop.onlineshop.service;

import com.shop.onlineshop.dto.UserDTO;
import com.shop.onlineshop.entity.RoleEntity;
import com.shop.onlineshop.entity.UserEntity;
import com.shop.onlineshop.repository.RoleRepository;
import com.shop.onlineshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDTO mapToDTO(UserEntity entity) {
        return new UserDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getRoles().stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public UserDTO createUser(String username, String password, Set<String> roleNames) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Set<RoleEntity> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseGet(() -> roleRepository.save(new RoleEntity(null, name))))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        UserEntity saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
