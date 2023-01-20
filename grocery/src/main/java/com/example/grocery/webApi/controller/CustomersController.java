package com.example.grocery.webApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocery.business.abstracts.CustomerService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.webApi.responses.customer.GetAllCustomerResponse;
import com.example.grocery.webApi.responses.customer.GetByIdCustomerResponse;

@RestController
@RequestMapping("/api/customer")
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
}
