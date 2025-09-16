package com.shop.onlineshop.controller;

import com.shop.onlineshop.dto.ProductDTO;
import com.shop.onlineshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @RestController: đánh dấu lớp xử lý HTTP request, trả JSON.
 * Constructor injection: Spring tự "bơm" (inject) ProductService vào controller.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // Spring sẽ tìm bean ProductService (ProductServiceImpl) và inject vào đây
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** CREATE: POST /products
     *  @RequestBody: đọc JSON trong body -> map sang ProductDTO
     *  Trả 201 Created + Location header best practice
     */
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product) {
        ProductDTO created = productService.createProduct(product);
        // Location: /products/{id}
        return ResponseEntity
                .created(URI.create("/products/" + created.getId())) // 201 Created
                .body(created);
    }

    /** READ-ALL: GET /products */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts()); // 200 OK
    }

    /** READ-ONE: GET /products/{id}
     *  @PathVariable: lấy {id} từ URL
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return (product != null) ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    /** UPDATE (full): PUT /products/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO product) {
        ProductDTO updated = productService.updateProduct(id, product);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /** DELETE: DELETE /products/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
    }
}
