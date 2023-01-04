package com.example.grocery.webApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocery.business.abstracts.UserService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.webApi.responses.user.GetAllUserResponse;
import com.example.grocery.webApi.responses.user.GetByIdUserResponse;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllUserResponse>>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdUserResponse>> getById(@RequestParam int id) {
        return ResponseEntity.ok().body(userService.getById(id));
    }
}
