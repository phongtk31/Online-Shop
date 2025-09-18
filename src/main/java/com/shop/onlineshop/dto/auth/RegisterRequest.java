package com.shop.onlineshop.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.swing.plaf.synth.SynthTreeUI;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Email
    @NotBlank
    private String email;
}
