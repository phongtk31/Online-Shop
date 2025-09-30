package com.shop.onlineshop.service;

import com.shop.onlineshop.dto.ProductDTO;
import com.shop.onlineshop.entity.ProductEntity;
import com.shop.onlineshop.exception.ResourceNotFoundException;
import com.shop.onlineshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    // ✅ Test create product
    @Test
    void testCreateProduct() {
        // given
        ProductEntity returnedEntity = new ProductEntity(1L, "iPhone 15", 1200.0, 5);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(returnedEntity);

        ProductDTO dto = new ProductDTO(null, "iPhone 15", 1200.0, 5);

        // when
        ProductDTO saved = productService.createProduct(dto);

        // then
        assertNotNull(saved.getId());
        assertEquals("iPhone 15", saved.getName());

        // kiểm tra entity truyền vào repo
        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productRepository, times(1)).save(captor.capture());

        ProductEntity captured = captor.getValue();
        assertNull(captured.getId()); // khi save mới id=null
        assertEquals("iPhone 15", captured.getName());
        assertEquals(1200.0, captured.getPrice());
        assertEquals(5, captured.getQuantity());
    }

    // ✅ Test get product by id (tìm thấy)
    @Test
    void testGetProductById_Found() {
        ProductEntity product = new ProductEntity(1L, "Samsung S24", 1100.0, 3);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Samsung S24", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    // ✅ Test get product by id (không tìm thấy)
    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(99L));

        verify(productRepository, times(1)).findById(99L);
    }

    // ✅ Test get all products
    @Test
    void testGetAllProducts() {
        List<ProductEntity> entities = Arrays.asList(
                new ProductEntity(1L, "iPhone 15", 1200.0, 5),
                new ProductEntity(2L, "Samsung S24", 1100.0, 3)
        );
        when(productRepository.findAll()).thenReturn(entities);

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("iPhone 15", result.get(0).getName());
        assertEquals("Samsung S24", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    // ✅ Test update product (tồn tại)
    @Test
    void testUpdateProduct_Found() {
        ProductEntity existing = new ProductEntity(1L, "iPhone 14", 1000.0, 2);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        ProductEntity updatedEntity = new ProductEntity(1L, "iPhone 15", 1200.0, 5);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(updatedEntity);

        ProductDTO dto = new ProductDTO(null, "iPhone 15", 1200.0, 5);
        ProductDTO result = productService.updateProduct(1L, dto);

        assertNotNull(result);
        assertEquals("iPhone 15", result.getName());
        assertEquals(1200.0, result.getPrice());
        assertEquals(5, result.getQuantity());

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    // ✅ Test update product (không tìm thấy)
    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ProductDTO dto = new ProductDTO(null, "Macbook Pro", 2500.0, 1);

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(99L, dto));

        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    // ✅ Test delete product (tồn tại)
    @Test
    void testDeleteProduct_Found() {
        ProductEntity product = new ProductEntity(1L, "Dell Laptop", 900.0, 2);
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    // ✅ Test delete product (không tìm thấy)
    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(99L)).thenReturn(false); 

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(99L));

        verify(productRepository, times(1)).existsById(99L);  // được gọi 1 lần
        verify(productRepository, never()).deleteById(anyLong()); // đảm bảo không xóa
    }

}
