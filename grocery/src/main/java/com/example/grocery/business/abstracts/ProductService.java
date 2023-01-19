package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.entity.concretes.Product;
import com.example.grocery.webApi.requests.product.CreateProductRequest;
import com.example.grocery.webApi.requests.product.DeleteProductRequest;
import com.example.grocery.webApi.requests.product.UpdateProductRequest;
import com.example.grocery.webApi.responses.product.GetAllProductResponse;
import com.example.grocery.webApi.responses.product.GetByIdProductResponse;

public interface ProductService {

    Result add(CreateProductRequest createProductRequest);

    Result update(UpdateProductRequest updateProductRequest, Long id);

    Result delete(DeleteProductRequest deleteProductRequest);

    DataResult<List<GetAllProductResponse>> getAll();

    DataResult<GetByIdProductResponse> getById(Long id);

    Product getProductById(Long id);

    List<Product> getProductsByIds(Long[] productId);
}
