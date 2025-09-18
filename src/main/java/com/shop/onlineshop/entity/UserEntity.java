package com.shop.onlineshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore // không cho password lộ ra ngoài response
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER) // load roles ngay khi load user
    @JoinTable(
            name = "user_roles", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"), // FK tới User
            inverseJoinColumns = @JoinColumn(name = "role_id") // FK tới Role
    )
    private Set<RoleEntity> roles = new HashSet<>();
}
