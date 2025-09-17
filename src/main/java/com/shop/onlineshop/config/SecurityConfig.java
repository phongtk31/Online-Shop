package com.shop.onlineshop.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Configuration: class config Spring
 * @Bean: khai báo bean trong Spring Context
 * SecurityFilterChain: định nghĩa rule bảo mật
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // tắt CSRF cho REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // cho phép public
                        .requestMatchers("/users/**").hasRole("ADMIN")   // chỉ ADMIN
                        .requestMatchers("/products/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/info/**").permitAll() // cho phép public
                        .anyRequest().authenticated()           // các request khác phải login
                )
                // 👇 Thêm dòng này để trả về 401 nếu không có/không đúng token
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        )
                );
                http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
