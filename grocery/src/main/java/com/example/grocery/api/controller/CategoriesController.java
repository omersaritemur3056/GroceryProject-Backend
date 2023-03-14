package com.example.grocery.api.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.grocery.service.interfaces.CategoryService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.category.CreateCategoryRequest;
import com.example.grocery.api.requests.category.DeleteCategoryRequest;
import com.example.grocery.api.requests.category.UpdateCategoryRequest;
import com.example.grocery.api.responses.category.GetAllCategoryResponse;
import com.example.grocery.api.responses.category.GetByIdCategoryResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/category")
@CrossOrigin
@AllArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Result> add(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.ok().body(categoryService.add(createCategoryRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<Result> update(@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest,
            @RequestParam Long id) {
        return ResponseEntity.ok().body(categoryService.update(updateCategoryRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result> delete(@Valid @RequestBody DeleteCategoryRequest deleteCategoryRequest) {
        return ResponseEntity.ok().body(categoryService.delete(deleteCategoryRequest));
    }

    @GetMapping("/getall")
    public ResponseEntity<DataResult<List<GetAllCategoryResponse>>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/getbyid")
    public ResponseEntity<DataResult<GetByIdCategoryResponse>> getById(@RequestParam Long id) {
        return ResponseEntity.ok().body(categoryService.getById(id));
    }

    @GetMapping("/getlistbysorting")
    public ResponseEntity<DataResult<List<GetAllCategoryResponse>>> getListBySorting(
            @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.ok(categoryService.getListBySorting(sortBy));
    }

    @GetMapping("/getlistbypagination")
    public ResponseEntity<DataResult<List<GetAllCategoryResponse>>> getListByPagination(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(categoryService.getListByPagination(pageNo, pageSize));
    }

    @GetMapping("/getlistbypaginationandsorting")
    public ResponseEntity<DataResult<List<GetAllCategoryResponse>>> getListByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(categoryService.getListByPaginationAndSorting(pageNo, pageSize, sortBy));
    }
}
