package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CustomerService;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.LogMessages.LogWarnMessages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.dataAccess.abstracts.CustomerRepository;
import com.example.grocery.entity.concretes.Customer;
import com.example.grocery.webApi.responses.customer.GetAllCustomerResponse;
import com.example.grocery.webApi.responses.customer.GetByIdCustomerResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerManager implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MapperService mapperService;

    @Override
    public DataResult<List<GetAllCustomerResponse>> getAll() {
        List<Customer> inDbCustomers = customerRepository.findAll();
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            obj.setUserId(forEachCustomer.getUser().getId());
            obj.setImageId(forEachCustomer.getImage().getId());
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
        returnObj.setImageId(inDbCustomer.getImage().getId());
        return new SuccessDataResult<>(returnObj, GetByIdMessages.CUSTOMER_LISTED);
    }

    @Override
    public DataResult<List<GetAllCustomerResponse>> getListBySorting(String sortBy) {
        isValidSortParameter(sortBy);

        List<Customer> inDbCustomers = customerRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            obj.setUserId(forEachCustomer.getUser().getId());
            obj.setImageId(forEachCustomer.getImage().getId());
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.CUSTOMERS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllCustomerResponse>> getListByPagination(int pageNo, int pageSize) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);

        List<Customer> inDbCustomers = customerRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            obj.setUserId(forEachCustomer.getUser().getId());
            obj.setImageId(forEachCustomer.getImage().getId());
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.CUSTOMERS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllCustomerResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);
        isValidSortParameter(sortBy);

        List<Customer> inDbCustomers = customerRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllCustomerResponse> returnList = new ArrayList<>();
        for (Customer forEachCustomer : inDbCustomers) {
            GetAllCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                    GetAllCustomerResponse.class);
            obj.setUserId(forEachCustomer.getUser().getId());
            obj.setImageId(forEachCustomer.getImage().getId());
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

    private void isPageNumberValid(int pageNo) {
        if (pageNo < 0) {
            log.warn(LogWarnMessages.PAGE_NUMBER_NEGATIVE);
            throw new BusinessException(ErrorMessages.PAGE_NUMBER_NEGATIVE);
        }
    }

    private void isPageSizeValid(int pageSize) {
        if (pageSize < 1) {
            log.warn(LogWarnMessages.PAGE_SIZE_NEGATIVE);
            throw new BusinessException(ErrorMessages.PAGE_SIZE_NEGATIVE);
        }
    }

    private void isValidSortParameter(String sortBy) {
        Customer checkField = new Customer();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }

}
