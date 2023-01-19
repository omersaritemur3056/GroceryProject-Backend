package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.entity.concretes.Category;
import com.example.grocery.webApi.requests.category.CreateCategoryRequest;
import com.example.grocery.webApi.requests.category.DeleteCategoryRequest;
import com.example.grocery.webApi.requests.category.UpdateCategoryRequest;
import com.example.grocery.webApi.responses.category.GetAllCategoryResponse;
import com.example.grocery.webApi.responses.category.GetByIdCategoryResponse;

public interface CategoryService {

    Result add(CreateCategoryRequest createCategoryRequest);

    Result delete(DeleteCategoryRequest deleteCategoryRequest);

    Result update(UpdateCategoryRequest updateCategoryRequest, Long id);

    DataResult<List<GetAllCategoryResponse>> getAll();

    DataResult<GetByIdCategoryResponse> getById(Long id);

    default Category getCategoryById(Long id) {
        return null;
    }
}
