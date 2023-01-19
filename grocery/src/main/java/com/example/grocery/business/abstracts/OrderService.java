package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.order.CreateOrderRequest;
import com.example.grocery.webApi.requests.order.DeleteOrderRequest;
import com.example.grocery.webApi.requests.order.UpdateOrderRequest;
import com.example.grocery.webApi.responses.order.GetAllOrderResponse;
import com.example.grocery.webApi.responses.order.GetByIdOrderResponse;

public interface OrderService {

    Result add(CreateOrderRequest createOrderRequest);

    Result delete(DeleteOrderRequest deleteOrderRequest);

    Result update(UpdateOrderRequest updateOrderRequest, Long id);

    DataResult<List<GetAllOrderResponse>> getAll();

    DataResult<GetByIdOrderResponse> getById(Long id);
}
