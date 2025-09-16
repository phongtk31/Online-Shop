package com.shop.onlineshop.repository;

import com.shop.onlineshop.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JpaRepository<T, ID> là generic:
 *  - T = Entity
 *  - ID = kiểu của @Id (ở đây là Long)
 * => Spring Data JPA sẽ tự generate CRUD cơ bản (findAll, findById, save, deleteById)
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
