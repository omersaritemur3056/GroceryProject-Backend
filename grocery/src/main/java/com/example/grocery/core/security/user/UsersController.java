package com.example.grocery.core.security.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocery.core.security.DTOs.GetAllUserResponseDto;
import com.example.grocery.core.security.DTOs.GetByIdUserResponseDto;
import com.example.grocery.core.utilities.results.DataResult;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllUserResponseDto>>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdUserResponseDto>> getById(@RequestParam int id) {
        return ResponseEntity.ok().body(userService.getById(id));
    }
}
