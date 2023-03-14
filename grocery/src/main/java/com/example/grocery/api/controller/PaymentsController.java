package com.example.grocery.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.service.interfaces.PaymentService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.payment.CreatePaymentRequest;
import com.example.grocery.api.requests.payment.DeletePaymentRequest;
import com.example.grocery.api.requests.payment.UpdatePaymentRequest;
import com.example.grocery.api.responses.payment.GetAllPaymentResponse;
import com.example.grocery.api.responses.payment.GetByIdPaymentResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentsController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(@Valid @RequestBody CreatePaymentRequest createPaymentRequest) {
        return ResponseEntity.ok().body(paymentService.add(createPaymentRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(@Valid @RequestBody DeletePaymentRequest deletePaymentRequest) {
        return ResponseEntity.ok().body(paymentService.delete(deletePaymentRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody UpdatePaymentRequest updatePaymentRequest, Long id) {
        return ResponseEntity.ok().body(paymentService.update(updatePaymentRequest, id));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllPaymentResponse>>> getAll() {
        return new ResponseEntity<>(paymentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdPaymentResponse>> getById(@RequestParam Long id) {
        return new ResponseEntity<>(paymentService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllPaymentResponse>>> getListBySorting(
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(paymentService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllPaymentResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(paymentService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllPaymentResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(paymentService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
