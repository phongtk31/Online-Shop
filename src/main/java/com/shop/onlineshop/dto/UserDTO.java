package com.shop.onlineshop.dto;

import com.shop.onlineshop.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private Set<String> roles; // chỉ trả tên role thay vì full object
}
