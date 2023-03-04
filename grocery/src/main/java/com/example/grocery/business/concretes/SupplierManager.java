package com.example.grocery.business.concretes;

import java.util.List;

import com.example.grocery.business.rules.SupplierBusinessRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.SupplierService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.UpdateMessages;
import com.example.grocery.business.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.SupplierRepository;
import com.example.grocery.entity.concretes.Supplier;
import com.example.grocery.webApi.requests.supplier.CreateSupplierRequest;
import com.example.grocery.webApi.requests.supplier.DeleteSupplierRequest;
import com.example.grocery.webApi.requests.supplier.UpdateSupplierRequest;
import com.example.grocery.webApi.responses.supplier.GetAllSupplierResponse;
import com.example.grocery.webApi.responses.supplier.GetByIdSupplierResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupplierManager implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private MapperService mapperService;
    @Autowired
    private SupplierBusinessRules supplierBusinessRules;

    @Override
    public Result add(CreateSupplierRequest createSupplierRequest) {

        Result rules = BusinessRules.run(supplierBusinessRules.isExistEmail(createSupplierRequest.getEmail()),
                supplierBusinessRules.isExistName(createSupplierRequest.getName()),
                supplierBusinessRules.isExistPhoneNumber(createSupplierRequest.getPhoneNumber()));
        if (!rules.isSuccess())
            return rules;

        Supplier supplier = mapperService.forRequest().map(createSupplierRequest, Supplier.class);
        supplierRepository.save(supplier);
        log.info(LogInfoMessages.SUPPLIER_ADDED, createSupplierRequest.getName());
        return new SuccessResult(CreateMessages.SUPPLIER_CREATED);
    }

    @Override
    public Result update(UpdateSupplierRequest updateSupplierRequest, Long id) {

        Result rules = BusinessRules.run(supplierBusinessRules.isExistEmail(updateSupplierRequest.getEmail()),
                supplierBusinessRules.isExistName(updateSupplierRequest.getName()),
                supplierBusinessRules.isExistPhoneNumber(updateSupplierRequest.getPhoneNumber()),
                supplierBusinessRules.isExistId(id));
        if (!rules.isSuccess())
            return rules;

        var inDbSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        Supplier supplier = mapperService.forRequest().map(updateSupplierRequest, Supplier.class);
        supplier.setId(inDbSupplier.getId());
        supplierRepository.save(supplier);
        log.info(LogInfoMessages.SUPPLIER_UPDATED, updateSupplierRequest.getName());
        return new SuccessResult(UpdateMessages.SUPPLIER_MODIFIED);
    }

    @Override
    public Result delete(DeleteSupplierRequest deleteSupplierRequest) {

        Result rules = BusinessRules.run(supplierBusinessRules.isExistId(deleteSupplierRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Supplier supplier = mapperService.forRequest().map(deleteSupplierRequest, Supplier.class);
        log.info(LogInfoMessages.SUPPLIER_DELETED, getSupplierById(deleteSupplierRequest.getId()).getName());
        supplierRepository.delete(supplier);
        return new SuccessResult(DeleteMessages.SUPPLIER_DELETED);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.forResponse().map(s, GetAllSupplierResponse.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.SUPPLIERS_LISTED);
    }

    @Override
    public DataResult<GetByIdSupplierResponse> getById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdSupplierResponse getByIdSupplierResponse = mapperService.forResponse().map(supplier,
                GetByIdSupplierResponse.class);
        return new SuccessDataResult<>(getByIdSupplierResponse, GetByIdMessages.SUPPLIER_LISTED);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getListBySorting(String sortBy) {
        supplierBusinessRules.isValidSortParameter(sortBy);

        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.forResponse().map(s, GetAllSupplierResponse.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.SUPPLIERS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getListByPagination(int pageNo, int pageSize) {
        supplierBusinessRules.isPageNumberValid(pageNo);
        supplierBusinessRules.isPageSizeValid(pageSize);

        List<Supplier> suppliers = supplierRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.forResponse().map(s, GetAllSupplierResponse.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.SUPPLIERS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        supplierBusinessRules.isPageNumberValid(pageNo);
        supplierBusinessRules.isPageSizeValid(pageSize);
        supplierBusinessRules.isValidSortParameter(sortBy);

        List<Supplier> suppliers = supplierRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.forResponse().map(s, GetAllSupplierResponse.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.SUPPLIERS_PAGINATED_AND_SORTED + sortBy);
    }

    // ProductManager sınıfımızda bağımlılığı kontrol altına alma adına kullanılmak
    // üzere tasarlandı.
    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.SUPPLIER_ID_NOT_FOUND));
    }

}
