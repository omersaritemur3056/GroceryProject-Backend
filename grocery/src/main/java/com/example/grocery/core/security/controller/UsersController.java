package com.example.grocery.core.security.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.core.security.DTOs.request.TokenRefreshRequest;
import com.example.grocery.core.security.DTOs.request.UpdateUserRequestDto;
import com.example.grocery.core.security.DTOs.request.UserForLoginDto;
import com.example.grocery.core.security.DTOs.request.UserForRegisterDto;
import com.example.grocery.core.security.DTOs.response.GetAllUserResponseDto;
import com.example.grocery.core.security.DTOs.response.GetByIdUserResponseDto;
import com.example.grocery.core.security.DTOs.response.JwtResponse;
import com.example.grocery.core.security.DTOs.response.TokenRefreshResponse;
import com.example.grocery.core.security.services.UserService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@CrossOrigin
public class UsersController {

    private final UserService userService;

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllUserResponseDto>>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdUserResponseDto>> getById(@RequestParam Long id) {
        return ResponseEntity.ok().body(userService.getById(id));
    }

    @PostMapping("/signup")
    public ResponseEntity<Result> register(@Valid @RequestBody UserForRegisterDto userForRegisterDto) {
        return ResponseEntity.ok(userService.register(userForRegisterDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<DataResult<JwtResponse>> login(@Valid @RequestBody UserForLoginDto userForLoginDto) {
        return ResponseEntity.ok(userService.login(userForLoginDto));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<DataResult<TokenRefreshResponse>> refreshtoken(
            @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return ResponseEntity.ok(userService.refreshtoken(tokenRefreshRequest));
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<Result> update(@Valid @PathVariable Long id,
            @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        return ResponseEntity.ok(userService.update(id, updateUserRequestDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(userService.delete(id));
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllUserResponseDto>>> getListBySorting(
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(userService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllUserResponseDto>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(userService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllUserResponseDto>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(userService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }

}
