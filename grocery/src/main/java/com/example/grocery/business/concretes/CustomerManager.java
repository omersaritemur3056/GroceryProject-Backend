package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;

import com.example.grocery.business.rules.CustomerBusinessRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CustomerService;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
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
    private CustomerBusinessRules customerBusinessRules;

    @Override
    public DataResult<List<GetAllCustomerResponse>> getAll() {
        List<Customer> inDbCustomers = customerRepository.findAll();
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.CUSTOMERS_LISTED);
    }

    @Override
    public DataResult<GetByIdCustomerResponse> getById(Long id) {
        Customer inDbCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdCustomerResponse returnObj = mapperService.forResponse().map(inDbCustomer,
                GetByIdCustomerResponse.class);
        return new SuccessDataResult<>(returnObj, GetByIdMessages.CUSTOMER_LISTED);
    }

    @Override
    public DataResult<List<GetAllCustomerResponse>> getListBySorting(String sortBy) {
        customerBusinessRules.isValidSortParameter(sortBy);

        List<Customer> inDbCustomers = customerRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.CUSTOMERS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllCustomerResponse>> getListByPagination(int pageNo, int pageSize) {
        customerBusinessRules.isPageNumberValid(pageNo);
        customerBusinessRules.isPageSizeValid(pageSize);

        List<Customer> inDbCustomers = customerRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.CUSTOMERS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllCustomerResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        customerBusinessRules.isPageNumberValid(pageNo);
        customerBusinessRules.isPageSizeValid(pageSize);
        customerBusinessRules.isValidSortParameter(sortBy);

        List<Customer> inDbCustomers = customerRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.CUSTOMERS_PAGINATED_AND_SORTED + sortBy);
    }

    // Bağımlılığı kontrol altına almak için tasarlandı
    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.CUSTOMER_ID_NOT_FOUND));
    }

}
