package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.product.CreateProductRequest;
import com.example.grocery.webApi.requests.product.DeleteProductRequest;
import com.example.grocery.webApi.requests.product.UpdateProductRequest;
import com.example.grocery.webApi.responses.product.GetAllProductResponse;
import com.example.grocery.webApi.responses.product.GetByIdProductResponse;

public interface ProductService {

    Result add(CreateProductRequest createProductRequest);

    Result update(UpdateProductRequest updateProductRequest, int id);

    Result delete(DeleteProductRequest deleteProductRequest);

    DataResult<List<GetAllProductResponse>> getAll();

    DataResult<GetByIdProductResponse> getById(int id);
}
