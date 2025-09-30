package com.shop.onlineshop.controller;

import com.shop.onlineshop.config.AppProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Hello API", description = "API test server")
public class HelloController {

    private final AppProperties appProperties;

    public HelloController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
                "name", appProperties.getName(),
                "version", appProperties.getVersion(),
                "contactEmail", appProperties.getContact().getEmail()
        );
    }
}
