package com.example.grocery.webApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocery.business.abstracts.PaymentService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.payment.CreatePaymentRequest;
import com.example.grocery.webApi.requests.payment.DeletePaymentRequest;
import com.example.grocery.webApi.requests.payment.UpdatePaymentRequest;
import com.example.grocery.webApi.responses.payment.GetAllPaymentResponse;
import com.example.grocery.webApi.responses.payment.GetByIdPaymentResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payment")
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
}
