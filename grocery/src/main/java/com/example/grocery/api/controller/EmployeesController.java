package com.example.grocery.api.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.service.interfaces.EmployeeService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.employee.CreateEmployeeRequest;
import com.example.grocery.api.requests.employee.DeleteEmployeeRequest;
import com.example.grocery.api.requests.employee.UpdateEmployeeRequest;
import com.example.grocery.api.responses.employee.GetAllEmployeeResponse;
import com.example.grocery.api.responses.employee.GetByIdEmployeeResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin
@AllArgsConstructor
public class EmployeesController {

    private final EmployeeService employeeService;

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

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllEmployeeResponse>>> getListBySorting(
            @RequestParam(defaultValue = "firstName") String sortBy) {
        return ResponseEntity.ok(employeeService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllEmployeeResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(employeeService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllEmployeeResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(employeeService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
