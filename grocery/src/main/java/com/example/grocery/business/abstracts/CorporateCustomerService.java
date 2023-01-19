package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.example.grocery.webApi.requests.corporateCustomer.DeleteCorporateCustomerRequest;
import com.example.grocery.webApi.requests.corporateCustomer.UpdateCorporateCustomerRequest;
import com.example.grocery.webApi.responses.corporateCustomer.GetAllCorporateCustomerResponse;
import com.example.grocery.webApi.responses.corporateCustomer.GetByIdCorporateCustomerResponse;

public interface CorporateCustomerService {

    Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest);

    Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest);

    Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest, Long id);

    DataResult<List<GetAllCorporateCustomerResponse>> getAll();

    DataResult<GetByIdCorporateCustomerResponse> getById(Long id);
}
