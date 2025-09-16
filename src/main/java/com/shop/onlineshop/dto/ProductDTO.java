package com.shop.onlineshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    @NotBlank(message = "Product name is required")
    private String name;
    @Min(value = 0, message = "Price must be >= 0")
    private double price;
    @Min(value = 0, message = "Quantity must be >= 0")
    private int quantity;
}
