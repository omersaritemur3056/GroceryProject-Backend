package com.example.grocery.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.service.interfaces.CustomerService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.api.responses.customer.GetAllCustomerResponse;
import com.example.grocery.api.responses.customer.GetByIdCustomerResponse;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin
public class CustomersController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllCustomerResponse>>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdCustomerResponse>> getById(@RequestParam Long id) {
        return ResponseEntity.ok().body(customerService.getById(id));
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllCustomerResponse>>> getListBySorting(
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(customerService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllCustomerResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(customerService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllCustomerResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(customerService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
