package com.example.grocery.service.rules;

import com.example.grocery.service.interfaces.CategoryService;
import com.example.grocery.service.interfaces.ProducerService;
import com.example.grocery.service.interfaces.SupplierService;
import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.repository.ProductRepository;
import com.example.grocery.model.concretes.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@AllArgsConstructor
public class ProductBusinessRules {

    private final ProductRepository productRepository;
    private final ProducerService producerService;
    private final SupplierService supplierService;
    private final CategoryService categoryService;

    public void removeExpiratedProduct() {
        for (Product product : productRepository.findAll()) {
            if (product.getExpirationDate().isBefore(LocalDate.now())) {
                productRepository.delete(product);
            }
        }
    }

    public Result isExistId(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistName(String name) {
        if (productRepository.existsByNameIgnoreCase(name)) {
            log.warn(Messages.LogMessages.LogWarnMessages.PRODUCT_NAME_REPEATED, name);
            throw new BusinessException(Messages.ErrorMessages.PRODUCT_NAME_REPEATED);
        }
        return new SuccessResult();
    }

    public Result isExistCategoryId(Long categoryId) {
        if (categoryService.getCategoryById(categoryId) == null) {
            throw new BusinessException(Messages.ErrorMessages.CATEGORY_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistSupplierId(Long supplierId) {
        if (supplierService.getSupplierById(supplierId) == null) {
            throw new BusinessException(Messages.ErrorMessages.SUPPLIER_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public Result isExistProducerId(Long producerId) {
        if (producerService.getProducerById(producerId) == null) {
            throw new BusinessException(Messages.ErrorMessages.PRODUCER_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    public void isPageNumberValid(int pageNo) {
        if (pageNo < 0) {
            log.warn(Messages.LogMessages.LogWarnMessages.PAGE_NUMBER_NEGATIVE);
            throw new BusinessException(Messages.ErrorMessages.PAGE_NUMBER_NEGATIVE);
        }
    }

    public void isPageSizeValid(int pageSize) {
        if (pageSize < 1) {
            log.warn(Messages.LogMessages.LogWarnMessages.PAGE_SIZE_NEGATIVE);
            throw new BusinessException(Messages.ErrorMessages.PAGE_SIZE_NEGATIVE);
        }
    }

    public void isValidSortParameter(String sortBy) {
        Product checkField = new Product();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(Messages.ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }
}
