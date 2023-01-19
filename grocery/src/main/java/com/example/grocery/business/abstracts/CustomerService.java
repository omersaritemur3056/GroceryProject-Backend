package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.entity.concretes.Customer;
import com.example.grocery.webApi.responses.customer.GetAllCustomerResponse;
import com.example.grocery.webApi.responses.customer.GetByIdCustomerResponse;

public interface CustomerService {

    DataResult<List<GetAllCustomerResponse>> getAll();

    DataResult<GetByIdCustomerResponse> getById(Long id);

    Customer getCustomerById(Long id);
}
