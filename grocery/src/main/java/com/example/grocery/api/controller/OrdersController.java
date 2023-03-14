package com.example.grocery.webApi.controller;

import com.example.grocery.business.abstracts.OrderService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.order.CreateOrderRequest;
import com.example.grocery.webApi.requests.order.DeleteOrderRequest;
import com.example.grocery.webApi.requests.order.UpdateOrderRequest;
import com.example.grocery.webApi.responses.order.GetAllOrderResponse;
import com.example.grocery.webApi.responses.order.GetByIdOrderResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
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

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<DataResult<GetByIdOrderResponse>> getById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllOrderResponse>>> getListBySorting(
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(orderService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllOrderResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(orderService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllOrderResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(orderService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
