package com.shop.onlineshop;

import com.shop.onlineshop.entity.RoleEntity;
import com.shop.onlineshop.entity.UserEntity;
import com.shop.onlineshop.repository.RoleRepository;
import com.shop.onlineshop.repository.UserRepository;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintStream;
import java.util.Set;

@SpringBootApplication
public class OnlineshopApplication {

	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OnlineshopApplication.class);
        app.run(args);
    }
}
