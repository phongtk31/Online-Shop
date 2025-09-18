package com.shop.onlineshop.config;

import com.shop.onlineshop.entity.RoleEntity;
import com.shop.onlineshop.entity.UserEntity;
import com.shop.onlineshop.repository.RoleRepository;
import com.shop.onlineshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            RoleEntity roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new RoleEntity(null, "ROLE_ADMIN")));
            RoleEntity roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new RoleEntity(null, "ROLE_USER")));

            UserEntity admin = UserEntity.builder()
                    .username("admin")
                    .password(encoder.encode("123456"))
                    .email("admin@shop.com")
                    .enabled(true)
                    .roles(Set.of(roleAdmin, roleUser))
                    .build();

            UserEntity user = UserEntity.builder()
                    .username("user")
                    .password(encoder.encode("user123"))
                    .email("user@shop.com")
                    .enabled(true)
                    .roles(Set.of(roleUser))
                    .build();

            userRepository.save(admin);
            userRepository.save(user);

            System.out.println("✅ Default users created: admin/123456, user/user123");
        }
    }
}
