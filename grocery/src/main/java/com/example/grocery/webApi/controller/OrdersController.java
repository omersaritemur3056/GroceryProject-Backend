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

import com.example.grocery.business.abstracts.OrderService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.order.CreateOrderRequest;
import com.example.grocery.webApi.requests.order.DeleteOrderRequest;
import com.example.grocery.webApi.requests.order.UpdateOrderRequest;
import com.example.grocery.webApi.responses.order.GetAllOrderResponse;
import com.example.grocery.webApi.responses.order.GetByIdOrderResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok().body(orderService.add(createOrderRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(@Valid @RequestBody DeleteOrderRequest deleteOrderRequest) {
        return ResponseEntity.ok().body(orderService.delete(deleteOrderRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody UpdateOrderRequest updateOrderRequest, Long id) {
        return ResponseEntity.ok().body(orderService.update(updateOrderRequest, id));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllOrderResponse>>> getAll() {
        return new ResponseEntity<>(orderService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdOrderResponse>> getById(@RequestParam Long id) {
        return new ResponseEntity<>(orderService.getById(id), HttpStatus.OK);
    }
}
