package com.example.grocery.webApi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.grocery.business.abstracts.CategoryService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.category.CreateCategoryRequest;
import com.example.grocery.webApi.requests.category.DeleteCategoryRequest;
import com.example.grocery.webApi.requests.category.UpdateCategoryRequest;
import com.example.grocery.webApi.responses.category.GetAllCategoryResponse;
import com.example.grocery.webApi.responses.category.GetByIdCategoryResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/category")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

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
}
