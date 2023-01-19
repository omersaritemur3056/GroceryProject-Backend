package com.example.grocery.webApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocery.business.abstracts.EmployeeService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.employee.CreateEmployeeRequest;
import com.example.grocery.webApi.requests.employee.DeleteEmployeeRequest;
import com.example.grocery.webApi.requests.employee.UpdateEmployeeRequest;
import com.example.grocery.webApi.responses.employee.GetAllEmployeeResponse;
import com.example.grocery.webApi.responses.employee.GetByIdEmployeeResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employee")
public class EmployeesController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(@Valid @RequestBody CreateEmployeeRequest createEmployeeRequest) {
        return ResponseEntity.ok().body(employeeService.add(createEmployeeRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody UpdateEmployeeRequest updateEmployeeRequest,
            @RequestParam Long id) {
        return ResponseEntity.ok().body(employeeService.update(updateEmployeeRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(@Valid @RequestBody DeleteEmployeeRequest deleteEmployeeRequest) {
        return ResponseEntity.ok().body(employeeService.delete(deleteEmployeeRequest));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllEmployeeResponse>>> getAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdEmployeeResponse>> getById(@RequestParam Long id) {
        return ResponseEntity.ok().body(employeeService.getById(id));
    }
}
