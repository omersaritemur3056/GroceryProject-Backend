package com.example.grocery.service.interfaces;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.model.concretes.Product;
import com.example.grocery.api.requests.product.CreateProductRequest;
import com.example.grocery.api.requests.product.DeleteProductRequest;
import com.example.grocery.api.requests.product.UpdateProductRequest;
import com.example.grocery.api.responses.product.GetAllProductResponse;
import com.example.grocery.api.responses.product.GetByIdProductResponse;

public interface ProductService {

    Result add(CreateProductRequest createProductRequest);

    Result update(UpdateProductRequest updateProductRequest, Long id);

    Result delete(DeleteProductRequest deleteProductRequest);

    DataResult<List<GetAllProductResponse>> getAll();

    DataResult<GetByIdProductResponse> getById(Long id);

    DataResult<List<GetAllProductResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllProductResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllProductResponse>> getListByPaginationAndSorting(int pageNo, int pageSize, String sortBy);

    Product getProductById(Long id);

    List<Product> getProductsByIds(Long[] productId);

    DataResult<List<GetAllProductResponse>> getAllByCategoryId(Long categoryId);
}
