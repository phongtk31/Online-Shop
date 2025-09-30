package com.shop.onlineshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.onlineshop.dto.auth.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // để convert object <-> JSON

    private String accessToken;

    @BeforeEach
    void loginAndGetToken() throws Exception {
        // 👇 login bằng user thật trong DB (ví dụ: admin / 123456)
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Lấy accessToken từ JSON
        accessToken = objectMapper.readTree(response)
                .path("data")
                .path("accessToken")
                .asText();
    }

    @Test
    void testGetProducts_WithRealJWT() throws Exception {
        mockMvc.perform(get("/products")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
