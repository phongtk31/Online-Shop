package com.shop.onlineshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Configuration: class config Spring
 * @Bean: khai báo bean trong Spring Context
 * SecurityFilterChain: định nghĩa rule bảo mật
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // tắt CSRF cho REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // cho phép public
                        .requestMatchers("/info/**").permitAll() // cho phép public
                        .anyRequest().authenticated()           // các request khác phải login
                )
                .httpBasic(Customizer.withDefaults()); // dùng Basic Auth (username/password)

        return http.build();
    }

    /**
     * UserDetailsService: nơi Spring Security tìm user
     * Ở đây tạo user in-memory để test
     */
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("123")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
