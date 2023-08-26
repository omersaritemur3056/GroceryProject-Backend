package com.example.grocery.service.implement;

import java.util.List;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.service.rules.SupplierBusinessRules;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.service.interfaces.SupplierService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.repository.SupplierRepository;
import com.example.grocery.model.concretes.Supplier;
import com.example.grocery.api.requests.supplier.CreateSupplierRequest;
import com.example.grocery.api.requests.supplier.DeleteSupplierRequest;
import com.example.grocery.api.requests.supplier.UpdateSupplierRequest;
import com.example.grocery.api.responses.supplier.GetAllSupplierResponse;
import com.example.grocery.api.responses.supplier.GetByIdSupplierResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final MapperService mapperService;
    private final SupplierBusinessRules supplierBusinessRules;

    @Override
    public Result add(CreateSupplierRequest createSupplierRequest) {
        Result rules = BusinessRules.run(supplierBusinessRules.isExistEmail(createSupplierRequest.getEmail()),
                supplierBusinessRules.isExistName(createSupplierRequest.getName()),
                supplierBusinessRules.isExistPhoneNumber(createSupplierRequest.getPhoneNumber()));
        if (!rules.isSuccess())
            return rules;

        Supplier supplier = mapperService.forRequest().map(createSupplierRequest, Supplier.class);
        supplierRepository.save(supplier);
        log.info(Messages.LogMessages.LogInfoMessages.SUPPLIER_ADDED, createSupplierRequest.getName());
        return new SuccessResult(Messages.CreateMessages.SUPPLIER_CREATED);
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
                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND));
        Supplier supplier = mapperService.forRequest().map(updateSupplierRequest, Supplier.class);
        supplier.setId(inDbSupplier.getId());
        supplierRepository.save(supplier);
        log.info(Messages.LogMessages.LogInfoMessages.SUPPLIER_UPDATED, updateSupplierRequest.getName());
        return new SuccessResult(Messages.UpdateMessages.SUPPLIER_MODIFIED);
    }

    @Override
    public Result delete(DeleteSupplierRequest deleteSupplierRequest) {
        Result rules = BusinessRules.run(supplierBusinessRules.isExistId(deleteSupplierRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Supplier supplier = mapperService.forRequest().map(deleteSupplierRequest, Supplier.class);
        log.info(Messages.LogMessages.LogInfoMessages.SUPPLIER_DELETED, getSupplierById(deleteSupplierRequest.getId()).getName());
        supplierRepository.delete(supplier);
        return new SuccessResult(Messages.DeleteMessages.SUPPLIER_DELETED);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.forResponse().map(s, GetAllSupplierResponse.class))
                .toList();
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.SUPPLIERS_LISTED);
    }

    @Override
    public DataResult<GetByIdSupplierResponse> getById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND));
        GetByIdSupplierResponse getByIdSupplierResponse = mapperService.forResponse().map(supplier,
                GetByIdSupplierResponse.class);
        return new SuccessDataResult<>(getByIdSupplierResponse, Messages.GetByIdMessages.SUPPLIER_LISTED);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getListBySorting(String sortBy) {

        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.forResponse().map(s, GetAllSupplierResponse.class))
                .toList();
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.SUPPLIERS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getListByPagination(int pageNo, int pageSize) {
        supplierBusinessRules.isPageNumberValid(pageNo);
        supplierBusinessRules.isPageSizeValid(pageSize);

        List<Supplier> suppliers = supplierRepository.findAll(PageRequest.of(pageNo, pageSize))
                .toList();
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.forResponse().map(s, GetAllSupplierResponse.class))
                .toList();
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.SUPPLIERS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        supplierBusinessRules.isPageNumberValid(pageNo);
        supplierBusinessRules.isPageSizeValid(pageSize);

        List<Supplier> suppliers = supplierRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy)))
                .toList();
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.forResponse().map(s, GetAllSupplierResponse.class))
                .toList();
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.SUPPLIERS_PAGINATED_AND_SORTED + sortBy);
    }

    // ProductManager sınıfımızda bağımlılığı kontrol altına alma adına kullanılmak
    // üzere tasarlandı.
    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.SUPPLIER_ID_NOT_FOUND));
    }

}
