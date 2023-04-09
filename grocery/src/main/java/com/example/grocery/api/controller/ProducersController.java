package com.example.grocery.api.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.service.interfaces.ProducerService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.producer.CreateProducerRequest;
import com.example.grocery.api.requests.producer.DeleteProducerRequest;
import com.example.grocery.api.requests.producer.UpdateProducerRequest;
import com.example.grocery.api.responses.producer.GetAllProducerResponse;
import com.example.grocery.api.responses.producer.GetByIdProducerResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/producer")
@CrossOrigin
@AllArgsConstructor
public class ProducersController {

    private final ProducerService producerService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(@Valid @RequestBody CreateProducerRequest createProducerRequest) {
        return ResponseEntity.ok().body(producerService.add(createProducerRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody UpdateProducerRequest updateProducerRequest,
            @RequestParam Long id) {
        return ResponseEntity.ok().body(producerService.update(updateProducerRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(@Valid @RequestBody DeleteProducerRequest deleteProducerRequest) {
        return ResponseEntity.ok().body(producerService.delete(deleteProducerRequest));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllProducerResponse>>> getAll() {
        return ResponseEntity.ok(producerService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdProducerResponse>> getById(@RequestParam Long id) {
        return new ResponseEntity<>(producerService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllProducerResponse>>> getListBySorting(
            @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.ok(producerService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllProducerResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(producerService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllProducerResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(producerService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
