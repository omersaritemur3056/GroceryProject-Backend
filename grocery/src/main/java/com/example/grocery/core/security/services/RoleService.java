package com.example.grocery.core.security.services;

import com.example.grocery.core.security.DTOs.response.GetAllRoleResponseDto;
import com.example.grocery.core.utilities.results.DataResult;

import java.util.List;

public interface RoleService {
    DataResult<List<GetAllRoleResponseDto>> getAll();
}
