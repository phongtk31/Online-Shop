package com.shop.onlineshop.controller;

import com.shop.onlineshop.dto.ProductDTO;
import com.shop.onlineshop.exception.ApiResponse;
import com.shop.onlineshop.exception.ResourceNotFoundException;
import com.shop.onlineshop.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> create(@Valid @RequestBody ProductDTO product) {
        ProductDTO created = productService.createProduct(product);
        return ResponseEntity
                .created(URI.create("/products/" + created.getId()))
                .body(ApiResponse.success("Product created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Product list", productService.getAllProducts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        return ResponseEntity.ok(ApiResponse.success("Product found", product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO product
    ) {
        ProductDTO updated = productService.updateProduct(id, product);
        if (updated == null) {
            throw new ResourceNotFoundException("Cannot update. Product not found with id " + id);
        }
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ProductDTO existing = productService.getProductById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Cannot delete. Product not found with id " + id);
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}
