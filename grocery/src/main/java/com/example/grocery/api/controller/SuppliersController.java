package com.example.grocery.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.service.interfaces.SupplierService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.supplier.CreateSupplierRequest;
import com.example.grocery.api.requests.supplier.DeleteSupplierRequest;
import com.example.grocery.api.requests.supplier.UpdateSupplierRequest;
import com.example.grocery.api.responses.supplier.GetAllSupplierResponse;
import com.example.grocery.api.responses.supplier.GetByIdSupplierResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/supplier")
@CrossOrigin
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

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllSupplierResponse>>> getListBySorting(
            @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.ok(supplierService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllSupplierResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(supplierService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllSupplierResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(supplierService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
