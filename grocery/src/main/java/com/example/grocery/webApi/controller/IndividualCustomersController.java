package com.example.grocery.webApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.business.abstracts.IndividualCustomerService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.individualCustomer.CreateIndividualCustomerRequest;
import com.example.grocery.webApi.requests.individualCustomer.DeleteIndividualCustomerRequest;
import com.example.grocery.webApi.requests.individualCustomer.UpdateIndividualCustomerRequest;
import com.example.grocery.webApi.responses.individualCustomer.GetAllIndividualCustomerResponse;
import com.example.grocery.webApi.responses.individualCustomer.GetByIdIndividualCustomerResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/individualcustomer")
@CrossOrigin
public class IndividualCustomersController {

    @Autowired
    private IndividualCustomerService individualCustomerService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(
            @Valid @RequestBody CreateIndividualCustomerRequest createIndividualCustomerRequest) {
        return ResponseEntity.ok().body(individualCustomerService.add(createIndividualCustomerRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(
            @Valid @RequestBody UpdateIndividualCustomerRequest updateIndividualCustomerRequest,
            @RequestParam Long id) {
        return ResponseEntity.ok().body(individualCustomerService.update(updateIndividualCustomerRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(
            @Valid @RequestBody DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {
        return ResponseEntity.ok().body(individualCustomerService.delete(deleteIndividualCustomerRequest));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllIndividualCustomerResponse>>> getAll() {
        return ResponseEntity.ok(individualCustomerService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdIndividualCustomerResponse>> getById(@RequestParam Long id) {
        return ResponseEntity.ok().body(individualCustomerService.getById(id));
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllIndividualCustomerResponse>>> getListBySorting(
            @RequestParam(defaultValue = "firstName") String sortBy) {
        return ResponseEntity.ok(individualCustomerService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllIndividualCustomerResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(individualCustomerService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllIndividualCustomerResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(individualCustomerService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
