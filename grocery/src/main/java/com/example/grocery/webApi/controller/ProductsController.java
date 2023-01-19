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

import com.example.grocery.business.abstracts.ProductService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.product.CreateProductRequest;
import com.example.grocery.webApi.requests.product.DeleteProductRequest;
import com.example.grocery.webApi.requests.product.UpdateProductRequest;
import com.example.grocery.webApi.responses.product.GetAllProductResponse;
import com.example.grocery.webApi.responses.product.GetByIdProductResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(@Valid @RequestBody CreateProductRequest createProductRequest) {
        return ResponseEntity.ok().body(productService.add(createProductRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody UpdateProductRequest updateProductRequest,
            @RequestParam Long id) {
        return ResponseEntity.ok().body(productService.update(updateProductRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(@Valid @RequestBody DeleteProductRequest deleteProductRequest) {
        return ResponseEntity.ok().body(productService.delete(deleteProductRequest));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllProductResponse>>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResult<GetByIdProductResponse>> getById(@RequestParam Long id) {
        return new ResponseEntity<>(productService.getById(id), HttpStatus.OK);
    }
}
