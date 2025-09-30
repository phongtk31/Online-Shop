package com.shop.onlineshop.service;

import com.shop.onlineshop.dto.ProductDTO;
import com.shop.onlineshop.entity.ProductEntity;
import com.shop.onlineshop.exception.ResourceNotFoundException;
import com.shop.onlineshop.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service chuyển từ in-memory sang JPA
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ProductDTO mapToDTO(ProductEntity entity) {
        return new ProductDTO(entity.getId(), entity.getName(), entity.getPrice(), entity.getQuantity());
    }

    private ProductEntity mapToEntity(ProductDTO dto) {
        return new ProductEntity(dto.getId(), dto.getName(), dto.getPrice(), dto.getQuantity());
    }

    @Override
    public ProductDTO createProduct(ProductDTO product) {
        ProductEntity entity = mapToEntity(product);
        ProductEntity saved = productRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return mapToDTO(entity);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update. Product not found with id " + id));

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());

        ProductEntity updated = productRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }
}
