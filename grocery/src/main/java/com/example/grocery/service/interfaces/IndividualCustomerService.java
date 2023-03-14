package com.example.grocery.service.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.individualCustomer.CreateIndividualCustomerRequest;
import com.example.grocery.api.requests.individualCustomer.DeleteIndividualCustomerRequest;
import com.example.grocery.api.requests.individualCustomer.UpdateIndividualCustomerRequest;
import com.example.grocery.api.responses.individualCustomer.GetAllIndividualCustomerResponse;
import com.example.grocery.api.responses.individualCustomer.GetByIdIndividualCustomerResponse;

public interface IndividualCustomerService {

    Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest);

    Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest);

    Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest, Long id);

    DataResult<List<GetAllIndividualCustomerResponse>> getAll();

    DataResult<GetByIdIndividualCustomerResponse> getById(Long id);

    DataResult<List<GetAllIndividualCustomerResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllIndividualCustomerResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllIndividualCustomerResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy);
}
