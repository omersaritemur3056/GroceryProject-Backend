package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CustomerService;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.core.security.services.UserService;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
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
    private MapperService mapperService;
    @Autowired
    private UserService userService;

    @Override
    public DataResult<List<GetAllCustomerResponse>> getAll() {
        List<Customer> inDbCustomers = customerRepository.findAll();
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            obj.setUserId(forEachCustomer.getUser().getId());
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.CUSTOMERS_LISTED);
    }

    @Override
    public DataResult<GetByIdCustomerResponse> getById(Long id) {
        Customer inDbCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdCustomerResponse returnObj = mapperService.getModelMapper().map(inDbCustomer,
                GetByIdCustomerResponse.class);
        returnObj.setUserId(inDbCustomer.getUser().getId());
        return new SuccessDataResult<>(returnObj, GetByIdMessages.CUSTOMER_LISTED);
    }

    // Bağımlılığı kontrol altına almak için tasarlandı
    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.CUSTOMER_ID_NOT_FOUND));
    }
}
