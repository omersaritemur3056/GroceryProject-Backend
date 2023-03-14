package com.example.grocery.service.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.entity.concretes.Category;
import com.example.grocery.api.requests.category.CreateCategoryRequest;
import com.example.grocery.api.requests.category.DeleteCategoryRequest;
import com.example.grocery.api.requests.category.UpdateCategoryRequest;
import com.example.grocery.api.responses.category.GetAllCategoryResponse;
import com.example.grocery.api.responses.category.GetByIdCategoryResponse;

public interface CategoryService {

    Result add(CreateCategoryRequest createCategoryRequest);

    Result delete(DeleteCategoryRequest deleteCategoryRequest);

    Result update(UpdateCategoryRequest updateCategoryRequest, Long id);

    DataResult<List<GetAllCategoryResponse>> getAll();

    DataResult<GetByIdCategoryResponse> getById(Long id);

    DataResult<List<GetAllCategoryResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllCategoryResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllCategoryResponse>> getListByPaginationAndSorting(int pageNo, int pageSize, String sortBy);

    default Category getCategoryById(Long id) {
        return null;
    }
}
