package com.example.grocery.webApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.business.abstracts.CorporateCustomerService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.example.grocery.webApi.requests.corporateCustomer.DeleteCorporateCustomerRequest;
import com.example.grocery.webApi.requests.corporateCustomer.UpdateCorporateCustomerRequest;
import com.example.grocery.webApi.responses.corporateCustomer.GetAllCorporateCustomerResponse;
import com.example.grocery.webApi.responses.corporateCustomer.GetByIdCorporateCustomerResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/corporatecustomer")
@CrossOrigin
public class CorporateCustomersController {

    @Autowired
    private CorporateCustomerService corporateCustomerService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(
            @Valid @RequestBody CreateCorporateCustomerRequest createCorporateCustomerRequest) {
        return ResponseEntity.ok().body(corporateCustomerService.add(createCorporateCustomerRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(
            @Valid @RequestBody UpdateCorporateCustomerRequest updateCorporateCustomerRequest,
            @RequestParam Long id) {
        return ResponseEntity.ok().body(corporateCustomerService.update(updateCorporateCustomerRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(
            @Valid @RequestBody DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) {
        return ResponseEntity.ok().body(corporateCustomerService.delete(deleteCorporateCustomerRequest));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllCorporateCustomerResponse>>> getAll() {
        return ResponseEntity.ok(corporateCustomerService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdCorporateCustomerResponse>> getById(@RequestParam Long id) {
        return ResponseEntity.ok().body(corporateCustomerService.getById(id));
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllCorporateCustomerResponse>>> getListBySorting(
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(corporateCustomerService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllCorporateCustomerResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(corporateCustomerService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllCorporateCustomerResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(corporateCustomerService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
