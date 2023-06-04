package com.example.grocery.core.security.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllRoleResponseDto {

    private Long id;

    private String name;
}
