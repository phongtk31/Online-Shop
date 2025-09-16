package com.shop.onlineshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Entity: đánh dấu class là một entity ánh xạ với table DB
 * @Table: đặt tên table (nếu không, mặc định = tên class)
 * @Id: khóa chính
 * @GeneratedValue: auto-increment
 * @Column: cấu hình cột DB
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;
}
