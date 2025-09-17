package com.shop.onlineshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.onlineshop.exception.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginAndAccessProtectedApi() throws Exception {
        // Step 1: Login
        String loginRequest = objectMapper.writeValueAsString(
                Map.of("username", "user", "password", "123")
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        ApiResponse<?> apiResponse = objectMapper.readValue(loginResponse, ApiResponse.class);
        assertThat(apiResponse.isSuccess()).isTrue();

        String token = (String) apiResponse.getData();
        assertThat(token).isNotBlank();

        // Step 2: Gọi API /products mà không có token → 401
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());

        // Step 3: Gọi API /products có token → OK
        mockMvc.perform(get("/products")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
