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

import com.example.grocery.business.abstracts.ProducerService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.producer.CreateProducerRequest;
import com.example.grocery.webApi.requests.producer.DeleteProducerRequest;
import com.example.grocery.webApi.requests.producer.UpdateProducerRequest;
import com.example.grocery.webApi.responses.producer.GetAllProducerResponse;
import com.example.grocery.webApi.responses.producer.GetByIdProducerResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/producer")
public class ProducersController {

    @Autowired
    private ProducerService producerService;

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
}
