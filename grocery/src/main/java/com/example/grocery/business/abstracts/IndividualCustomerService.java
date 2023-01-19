package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.individualCustomer.CreateIndividualCustomerRequest;
import com.example.grocery.webApi.requests.individualCustomer.DeleteIndividualCustomerRequest;
import com.example.grocery.webApi.requests.individualCustomer.UpdateIndividualCustomerRequest;
import com.example.grocery.webApi.responses.individualCustomer.GetAllIndividualCustomerResponse;
import com.example.grocery.webApi.responses.individualCustomer.GetByIdIndividualCustomerResponse;

public interface IndividualCustomerService {

    Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest);

    Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest);

    Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest, Long id);

    DataResult<List<GetAllIndividualCustomerResponse>> getAll();

    DataResult<GetByIdIndividualCustomerResponse> getById(Long id);
}
