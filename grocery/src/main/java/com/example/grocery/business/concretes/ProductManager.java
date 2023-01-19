package com.example.grocery.business.concretes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CategoryService;
import com.example.grocery.business.abstracts.ProducerService;
import com.example.grocery.business.abstracts.ProductService;
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
import com.example.grocery.dataAccess.abstracts.ProductRepository;
import com.example.grocery.entity.concretes.Product;
import com.example.grocery.webApi.requests.product.CreateProductRequest;
import com.example.grocery.webApi.requests.product.DeleteProductRequest;
import com.example.grocery.webApi.requests.product.UpdateProductRequest;
import com.example.grocery.webApi.responses.product.GetAllProductResponse;
import com.example.grocery.webApi.responses.product.GetByIdProductResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductManager implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProducerService producerService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private MapperService mapperService;

    @Override
    public Result add(CreateProductRequest createProductRequest) {

        Result rules = BusinessRules.run(isExistName(createProductRequest.getName()),
                isExistCategoryId(createProductRequest.getCategoryId()));

        Product addProduct = mapperService.getModelMapper().map(createProductRequest, Product.class);
        addProduct.setCategory(categoryService.getCategoryById(createProductRequest.getCategoryId()));
        addProduct.setProducer(producerService.getProducerById(createProductRequest.getProducerId()));
        addProduct.setSupplier(supplierService.getSupplierById(createProductRequest.getSupplierId()));
        productRepository.save((addProduct));
        log.info("added product: {} logged to file!", createProductRequest.getName());
        return new SuccessResult(CreateMessages.PRODUCT_CREATED);
    }

    @Override
    public Result update(UpdateProductRequest updateProductRequest, Long id) {

        Product inDbProduct = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Result rules = BusinessRules.run(isExistName(updateProductRequest.getName()), isExistId(id),
                isExistCategoryId(updateProductRequest.getCategoryId()),
                isExistProducerId(updateProductRequest.getProducerId()),
                isExistSupplierId(updateProductRequest.getSupplierId()));

        Product product = mapperService.getModelMapper().map(updateProductRequest, Product.class);
        product.setId(inDbProduct.getId());
        product.setCategory(categoryService.getCategoryById(updateProductRequest.getCategoryId()));
        product.setProducer(producerService.getProducerById(updateProductRequest.getProducerId()));
        product.setSupplier(supplierService.getSupplierById(updateProductRequest.getSupplierId()));
        log.info("modified product : {} logged to file!", updateProductRequest.getName());
        productRepository.save(product);

        return new SuccessResult(UpdateMessages.PRODUCT_MODIFIED);
    }

    @Override
    public Result delete(DeleteProductRequest deleteProductRequest) {

        Result rules = BusinessRules.run(isExistId(deleteProductRequest.getId()));
        removeExpiratedProduct();

        Product product = mapperService.getModelMapper().map(deleteProductRequest, Product.class);

        Product productForLog = productRepository.findById(deleteProductRequest.getId())
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        log.info("removed product: {} logged to file!", productForLog.getName());
        productRepository.delete(product);

        return new SuccessResult(DeleteMessages.PRODUCT_DELETED);
    }

    @Override
    public DataResult<List<GetAllProductResponse>> getAll() {
        List<GetAllProductResponse> returnList = new ArrayList<>();
        List<Product> productList = productRepository.findAll();
        for (Product product : productList) {
            Product product1 = productRepository.findById(product.getId())
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            GetAllProductResponse addFields = mapperService.getModelMapper().map(product,
                    GetAllProductResponse.class);
            addFields.setCategoryId(product1.getCategory().getId());
            addFields.setProducerId(product1.getProducer().getId());
            addFields.setSupplierId(product1.getSupplier().getId());
            returnList.add(addFields);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_LISTED);
    }

    @Override
    public DataResult<GetByIdProductResponse> getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdProductResponse getByIdProductResponse = mapperService.getModelMapper().map(product,
                GetByIdProductResponse.class);
        getByIdProductResponse.setCategoryId(product.getCategory().getId());
        getByIdProductResponse.setProducerId(product.getProducer().getId());
        getByIdProductResponse.setSupplierId(product.getSupplier().getId());
        return new SuccessDataResult<>(getByIdProductResponse, GetByIdMessages.PRODUCT_LISTED);
    }

    // bağımlılğı kontrol altına almak üzere tasarlandı
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.PRODUCT_ID_NOT_FOUND));
    }

    @Override
    public List<Product> getProductsByIds(Long[] productId) {
        List<Product> resultList = new ArrayList<>();
        for (Long forEachId : productId) {
            Product findProductById = productRepository.findById(forEachId)
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            resultList.add(findProductById);
        }
        return resultList;
    }

    private void removeExpiratedProduct() {
        for (Product product : productRepository.findAll()) {
            if (product.getExpirationDate().isBefore(LocalDate.now())) {
                productRepository.delete(product);
            }
        }
    }

    private Result isExistId(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistName(String name) {
        if (productRepository.existsByNameIgnoreCase(name)) {
            log.error("product name: {} couldn't saved", name);
            throw new BusinessException(ErrorMessages.PRODUCER_NAME_REPEATED);
        }
        return new SuccessResult();
    }

    private Result isExistCategoryId(Long categoryId) {
        if (categoryService.getCategoryById(categoryId) == null) {
            throw new BusinessException(ErrorMessages.CATEGORY_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistSupplierId(Long supplierId) {
        if (supplierService.getSupplierById(supplierId) == null) {
            throw new BusinessException(ErrorMessages.SUPPLIER_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistProducerId(Long producerId) {
        if (producerService.getProducerById(producerId) == null) {
            throw new BusinessException(ErrorMessages.PRODUCER_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

}
