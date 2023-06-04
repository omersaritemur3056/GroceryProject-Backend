package com.example.grocery.core.security.controller;

import com.example.grocery.core.security.DTOs.response.GetAllRoleResponseDto;
import com.example.grocery.core.security.services.RoleService;
import com.example.grocery.core.utilities.results.DataResult;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
@CrossOrigin
public class RolesController {
    private final RoleService roleService;

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllRoleResponseDto>>> getAll() {
        return ResponseEntity.ok(roleService.getAll());
    }
}
