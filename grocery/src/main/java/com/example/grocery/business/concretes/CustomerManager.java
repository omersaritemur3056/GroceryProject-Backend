package com.example.grocery.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CustomerService;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.modelMapper.ModelMapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.dataAccess.abstracts.CustomerRepository;
import com.example.grocery.entity.concretes.Customer;
import com.example.grocery.webApi.responses.customer.GetAllCustomerResponse;
import com.example.grocery.webApi.responses.customer.GetByIdCustomerResponse;

@Service
public class CustomerManager implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapperService modelMapperService;

    @Override
    public DataResult<List<GetAllCustomerResponse>> getAll() {
        List<Customer> inDbCustomers = customerRepository.findAll();
        List<GetAllCustomerResponse> returnList = inDbCustomers.stream()
                .map(c -> modelMapperService.getModelMapper().map(c, GetAllCustomerResponse.class)).toList();
        return new SuccessDataResult<List<GetAllCustomerResponse>>(returnList, "Customers listed");
    }

    @Override
    public DataResult<GetByIdCustomerResponse> getById(int id) {
        Customer inDbCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Id not found!"));
        GetByIdCustomerResponse returnObj = modelMapperService.getModelMapper().map(inDbCustomer,
                GetByIdCustomerResponse.class);
        return new SuccessDataResult<GetByIdCustomerResponse>(returnObj, "Customer listed by chosen id");
    }
}
