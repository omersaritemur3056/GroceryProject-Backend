package com.example.grocery.service.interfaces;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.order.CreateOrderRequest;
import com.example.grocery.api.requests.order.DeleteOrderRequest;
import com.example.grocery.api.requests.order.UpdateOrderRequest;
import com.example.grocery.api.responses.order.GetAllOrderResponse;
import com.example.grocery.api.responses.order.GetByIdOrderResponse;

public interface OrderService {

    Result add(CreateOrderRequest createOrderRequest);

    Result delete(DeleteOrderRequest deleteOrderRequest);

    Result update(UpdateOrderRequest updateOrderRequest, Long id);

    DataResult<List<GetAllOrderResponse>> getAll();

    DataResult<GetByIdOrderResponse> getById(Long id);

    DataResult<List<GetAllOrderResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllOrderResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllOrderResponse>> getListByPaginationAndSorting(int pageNo, int pageSize, String sortBy);
}
