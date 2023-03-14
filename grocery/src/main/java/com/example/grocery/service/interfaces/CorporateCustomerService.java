package com.example.grocery.service.interfaces;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.example.grocery.api.requests.corporateCustomer.DeleteCorporateCustomerRequest;
import com.example.grocery.api.requests.corporateCustomer.UpdateCorporateCustomerRequest;
import com.example.grocery.api.responses.corporateCustomer.GetAllCorporateCustomerResponse;
import com.example.grocery.api.responses.corporateCustomer.GetByIdCorporateCustomerResponse;

public interface CorporateCustomerService {

    Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest);

    Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest);

    Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest, Long id);

    DataResult<List<GetAllCorporateCustomerResponse>> getAll();

    DataResult<GetByIdCorporateCustomerResponse> getById(Long id);

    DataResult<List<GetAllCorporateCustomerResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllCorporateCustomerResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllCorporateCustomerResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy);
}
