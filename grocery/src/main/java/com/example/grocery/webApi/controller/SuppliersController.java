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

import com.example.grocery.business.abstracts.SupplierService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.supplier.CreateSupplierRequest;
import com.example.grocery.webApi.requests.supplier.DeleteSupplierRequest;
import com.example.grocery.webApi.requests.supplier.UpdateSupplierRequest;
import com.example.grocery.webApi.responses.supplier.GetAllSupplierResponse;
import com.example.grocery.webApi.responses.supplier.GetByIdSupplierResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/supplier")
public class SuppliersController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(@Valid @RequestBody CreateSupplierRequest createSupplierRequest) {
        return ResponseEntity.ok().body(supplierService.add(createSupplierRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody UpdateSupplierRequest updateSupplierRequest,
            @RequestParam Long id) {
        return ResponseEntity.ok().body(supplierService.update(updateSupplierRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(@Valid @RequestBody DeleteSupplierRequest deleteSupplierRequest) {
        return ResponseEntity.ok().body(supplierService.delete(deleteSupplierRequest));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllSupplierResponse>>> getAll() {
        return ResponseEntity.ok(supplierService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdSupplierResponse>> getById(@RequestParam Long id) {
        return ResponseEntity.ok().body(supplierService.getById(id));
    }
}
