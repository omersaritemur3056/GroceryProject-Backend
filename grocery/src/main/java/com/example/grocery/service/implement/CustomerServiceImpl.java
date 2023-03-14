package com.example.grocery.service.implement;

import java.util.ArrayList;
import java.util.List;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.service.rules.CustomerBusinessRules;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.service.interfaces.CustomerService;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.repository.CustomerRepository;
import com.example.grocery.model.concretes.Customer;
import com.example.grocery.api.responses.customer.GetAllCustomerResponse;
import com.example.grocery.api.responses.customer.GetByIdCustomerResponse;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final MapperService mapperService;
    private final CustomerBusinessRules customerBusinessRules;

    @Override
    public DataResult<List<GetAllCustomerResponse>> getAll() {
        List<Customer> inDbCustomers = customerRepository.findAll();
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.CUSTOMERS_LISTED);
    }

    @Override
    public DataResult<GetByIdCustomerResponse> getById(Long id) {
        Customer inDbCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND));
        GetByIdCustomerResponse returnObj = mapperService.forResponse().map(inDbCustomer,
                GetByIdCustomerResponse.class);
        return new SuccessDataResult<>(returnObj, Messages.GetByIdMessages.CUSTOMER_LISTED);
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
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.CUSTOMERS_SORTED + sortBy);
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
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.CUSTOMERS_PAGINATED);
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
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.CUSTOMERS_PAGINATED_AND_SORTED + sortBy);
    }

    // Bağımlılığı kontrol altına almak için tasarlandı
    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.CUSTOMER_ID_NOT_FOUND));
    }

}
