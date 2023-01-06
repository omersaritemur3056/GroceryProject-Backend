package com.example.grocery.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.SupplierService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.UpdateMessages;
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

    @Override
    public Result add(CreateSupplierRequest createSupplierRequest) {

        Result rules = BusinessRules.run(isExistEmail(createSupplierRequest.getEmail()),
                isExistName(createSupplierRequest.getName()),
                isExistPhoneNumber(createSupplierRequest.getPhoneNumber()));

        Supplier supplier = mapperService.getModelMapper().map(createSupplierRequest, Supplier.class);
        supplierRepository.save(supplier);
        log.info("succeed supplier : {} logged to file!", createSupplierRequest.getName());
        return new SuccessResult(CreateMessages.supplierCreated);
    }

    @Override
    public Result update(UpdateSupplierRequest updateSupplierRequest, int id) {

        Result rules = BusinessRules.run(isExistEmail(updateSupplierRequest.getEmail()),
                isExistName(updateSupplierRequest.getName()),
                isExistPhoneNumber(updateSupplierRequest.getPhoneNumber()),
                isExistId(id));

        var inDbSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.idNotFound));
        Supplier supplier = mapperService.getModelMapper().map(updateSupplierRequest, Supplier.class);
        supplier.setId(inDbSupplier.getId());
        supplierRepository.save(supplier);
        log.info("modified supplier : {} logged to file!", updateSupplierRequest.getName());
        return new SuccessResult(UpdateMessages.supplierModified);
    }

    @Override
    public Result delete(DeleteSupplierRequest deleteSupplierRequest) {

        Result rules = BusinessRules.run(isExistId(deleteSupplierRequest.getId()));

        Supplier supplier = mapperService.getModelMapper().map(deleteSupplierRequest, Supplier.class);
        log.info("removed supplier: {} logged to file!", getSupplierById(deleteSupplierRequest.getId()).getName());
        supplierRepository.delete(supplier);
        return new SuccessResult(DeleteMessages.supplierDeleted);
    }

    @Override
    public DataResult<List<GetAllSupplierResponse>> getAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<GetAllSupplierResponse> returnList = suppliers.stream()
                .map(s -> mapperService.getModelMapper().map(s, GetAllSupplierResponse.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.suppliersListed);
    }

    @Override
    public DataResult<GetByIdSupplierResponse> getById(int id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.idNotFound));
        GetByIdSupplierResponse getByIdSupplierResponse = mapperService.getModelMapper().map(supplier,
                GetByIdSupplierResponse.class);
        return new SuccessDataResult<>(getByIdSupplierResponse, GetByIdMessages.supplierListed);
    }

    // ProductManager sınıfımızda bağımlılığı kontrol altına alma adına kullanılmak
    // üzere tasarlandı.
    public Supplier getSupplierById(int id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.supplierIdNotFound));
    }

    private Result isExistId(int id) {
        if (!supplierRepository.existsById(id)) {
            throw new BusinessException(ErrorMessages.idNotFound);
        }
        return new SuccessResult();
    }

    private Result isExistName(String name) {
        if (supplierRepository.existsByName(name)) {
            throw new BusinessException(ErrorMessages.supplierNameRepeated);
        }
        return new SuccessResult();
    }

    private Result isExistEmail(String email) {
        if (supplierRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorMessages.emailRepeated);
        }
        return new SuccessResult();
    }

    private Result isExistPhoneNumber(String phoneNumber) {
        if (supplierRepository.existsByPhoneNumber(phoneNumber)) {
            throw new BusinessException(ErrorMessages.phoneNumberRepeated);
        }
        return new SuccessResult();
    }

}
