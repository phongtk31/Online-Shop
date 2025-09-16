package com.shop.onlineshop.service;

import com.shop.onlineshop.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Service: đăng ký bean tầng service vào Spring IoC container.
 * Dùng ConcurrentHashMap để tránh race-condition khi nhiều request cùng truy cập.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ConcurrentHashMap<Long, ProductDTO> productStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public ProductDTO createProduct(ProductDTO product) {
        long id = idCounter.getAndIncrement();
        product.setId(id);                 // gắn id sinh tự động
        productStore.put(id, product);     // lưu vào "RAM"
        return product;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return new ArrayList<>(productStore.values());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productStore.get(id);       // trả null nếu không có
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        if (!productStore.containsKey(id)) {
            return null;
        }
        product.setId(id);
        productStore.put(id, product);     // ghi đè
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        productStore.remove(id);
    }
}
