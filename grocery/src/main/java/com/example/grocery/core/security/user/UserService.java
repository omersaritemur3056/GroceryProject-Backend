package com.example.grocery.core.security.user;

import java.util.List;

import com.example.grocery.core.security.DTOs.GetAllUserResponseDto;
import com.example.grocery.core.security.DTOs.GetByIdUserResponseDto;
import com.example.grocery.core.utilities.results.DataResult;

public interface UserService {

    DataResult<List<GetAllUserResponseDto>> getAll();

    DataResult<GetByIdUserResponseDto> getById(int id);

    User getByEmail(String email);

    boolean existByEmail(String email);

    boolean existById(int id);
}
