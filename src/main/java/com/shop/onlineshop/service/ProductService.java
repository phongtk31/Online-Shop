package com.shop.onlineshop.service;

import com.shop.onlineshop.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO product);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);        // null nếu không tìm thấy (Day 4 sẽ ném exception “đẹp” hơn)
    ProductDTO updateProduct(Long id, ProductDTO product);
    void deleteProduct(Long id);
}
