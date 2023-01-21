package com.example.grocery.core.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocery.core.security.DTOs.request.TokenRefreshRequest;
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
@CrossOrigin(origins = "*")
public class UsersController {

    @Autowired
    private UserService userService;

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

    // @PostMapping("/signout")
    // public ResponseEntity<Result> signout() {
    // return ResponseEntity.ok(userService.signout());
    // }
}
