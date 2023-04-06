package com.example.grocery.api.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.example.grocery.service.interfaces.ProductService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.product.CreateProductRequest;
import com.example.grocery.api.requests.product.DeleteProductRequest;
import com.example.grocery.api.requests.product.UpdateProductRequest;
import com.example.grocery.api.responses.product.GetAllProductResponse;
import com.example.grocery.api.responses.product.GetByIdProductResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
@AllArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @PostMapping("/add")
    @Secured("ADMIN")
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
    public ResponseEntity<DataResult<List<GetAllProductResponse>>> getAll() throws InterruptedException {
        Thread.sleep(1000);
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResult<GetByIdProductResponse>> getById(@RequestParam Long id) {
        return new ResponseEntity<>(productService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllProductResponse>>> getListBySorting(
            @RequestParam(defaultValue = "name") String sortBy) throws InterruptedException {
        Thread.sleep(1000);
        return ResponseEntity.ok(productService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllProductResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize)
            throws InterruptedException{
        Thread.sleep(1000);
        return ResponseEntity.ok(productService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllProductResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy) throws InterruptedException {
        Thread.sleep(2000);
        return ResponseEntity.ok(productService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }

    @GetMapping("/getallbycategory")
    public ResponseEntity<DataResult<List<GetAllProductResponse>>> getAllByCategoryId(@RequestParam Long categoryId) {
        return new ResponseEntity<>(productService.getAllByCategoryId(categoryId), HttpStatus.OK);
    }
}
