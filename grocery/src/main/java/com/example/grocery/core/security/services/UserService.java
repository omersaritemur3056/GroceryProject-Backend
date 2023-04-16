package com.example.grocery.core.security.services;

import java.util.List;

import com.example.grocery.core.security.DTOs.request.*;
import com.example.grocery.core.security.DTOs.response.GetAllUserResponseDto;
import com.example.grocery.core.security.DTOs.response.GetByIdUserResponseDto;
import com.example.grocery.core.security.DTOs.response.JwtResponse;
import com.example.grocery.core.security.DTOs.response.TokenRefreshResponse;
import com.example.grocery.core.security.models.User;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;

public interface UserService {

    Result register(UserForRegisterDto userForRegisterDto);

    DataResult<JwtResponse> login(UserForLoginDto userForLoginDto);

    Result googleLogin(GoogleLoginRequest googleLoginRequest);

    Result update(Long id, UpdateUserRequestDto updateUserRequestDto);

    Result delete(Long id);

    DataResult<TokenRefreshResponse> refreshtoken(TokenRefreshRequest tokenRefreshRequest);

    DataResult<List<GetAllUserResponseDto>> getAll();

    DataResult<GetByIdUserResponseDto> getById(Long id);

    DataResult<List<GetAllUserResponseDto>> getListBySorting(String sortBy);

    DataResult<List<GetAllUserResponseDto>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllUserResponseDto>> getListByPaginationAndSorting(int pageNo, int pageSize, String sortBy);

    User getByEmail(String email);

    boolean existByEmail(String email);

    boolean existById(Long id);

    User getUserById(Long userId);
}
