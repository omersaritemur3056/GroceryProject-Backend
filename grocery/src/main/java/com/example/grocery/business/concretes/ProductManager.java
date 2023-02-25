package com.example.grocery.business.concretes;

import com.example.grocery.business.abstracts.*;
import com.example.grocery.business.constants.Messages.*;
import com.example.grocery.business.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.business.constants.Messages.LogMessages.LogWarnMessages;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.ProductRepository;
import com.example.grocery.entity.concretes.Image;
import com.example.grocery.entity.concretes.Product;
import com.example.grocery.webApi.requests.product.CreateProductRequest;
import com.example.grocery.webApi.requests.product.DeleteProductRequest;
import com.example.grocery.webApi.requests.product.UpdateProductRequest;
import com.example.grocery.webApi.responses.product.GetAllProductResponse;
import com.example.grocery.webApi.responses.product.GetByIdProductResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private PhotoService photoService;

    @Override
    @Transactional
    @CacheEvict(cacheNames = "product", allEntries = true)
    public Result add(CreateProductRequest createProductRequest) {

        Result rules = BusinessRules.run(isExistName(createProductRequest.getName()),
                isExistCategoryId(createProductRequest.getCategoryId()));
        if (!rules.isSuccess())
            return rules;

        Product addProduct = mapperService.getModelMapper().map(createProductRequest, Product.class);
        addProduct.setCategory(categoryService.getCategoryById(createProductRequest.getCategoryId()));
        addProduct.setProducer(producerService.getProducerById(createProductRequest.getProducerId()));
        addProduct.setSupplier(supplierService.getSupplierById(createProductRequest.getSupplierId()));
        addProduct.setImages(photoService.getImagesByIds(createProductRequest.getImageIds()));
        productRepository.save((addProduct));
        log.info(LogInfoMessages.PRODUCT_ADDED, createProductRequest.getName());
        return new SuccessResult(CreateMessages.PRODUCT_CREATED);
    }

    @Override
    @Transactional
    @CachePut(cacheNames = "product", key = "#id")
    public Result update(UpdateProductRequest updateProductRequest, Long id) {

        Product inDbProduct = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Result rules = BusinessRules.run(isExistName(updateProductRequest.getName()), isExistId(id),
                isExistCategoryId(updateProductRequest.getCategoryId()),
                isExistProducerId(updateProductRequest.getProducerId()),
                isExistSupplierId(updateProductRequest.getSupplierId()));
        if (!rules.isSuccess())
            return rules;

        Product product = mapperService.getModelMapper().map(updateProductRequest, Product.class);
        product.setId(inDbProduct.getId());
        product.setCategory(categoryService.getCategoryById(updateProductRequest.getCategoryId()));
        product.setProducer(producerService.getProducerById(updateProductRequest.getProducerId()));
        product.setSupplier(supplierService.getSupplierById(updateProductRequest.getSupplierId()));
        product.setImages(photoService.getImagesByIds(updateProductRequest.getImageIds()));
        log.info(LogInfoMessages.PRODUCT_UPDATED, updateProductRequest.getName());
        productRepository.save(product);

        return new SuccessResult(UpdateMessages.PRODUCT_MODIFIED);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "product", key = "#deleteProductRequest.id")
    public Result delete(DeleteProductRequest deleteProductRequest) {

        Result rules = BusinessRules.run(isExistId(deleteProductRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        removeExpiratedProduct();

        Product product = mapperService.getModelMapper().map(deleteProductRequest, Product.class);

        Product productForLog = productRepository.findById(deleteProductRequest.getId())
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        log.info(LogInfoMessages.PRODUCT_DELETED, productForLog.getName());
        productRepository.delete(product);

        return new SuccessResult(DeleteMessages.PRODUCT_DELETED);
    }

    @Override
    @Cacheable(value = "product")
    public DataResult<List<GetAllProductResponse>> getAll() {
        List<GetAllProductResponse> returnList = new ArrayList<>();
        List<Product> productList = productRepository.findAll();
        for (Product product : productList) {
            Product product1 = productRepository.findById(product.getId())
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            GetAllProductResponse addFields = mapperService.getModelMapper().map(product,
                    GetAllProductResponse.class);
            addFields.setCategoryId(product1.getCategory().getId());
            addFields.setCategoryName(product1.getCategory().getName());
            addFields.setProducerId(product1.getProducer().getId());
            addFields.setProducerName(product1.getProducer().getName());
            addFields.setSupplierId(product1.getSupplier().getId());
            addFields.setSupplierName(product1.getSupplier().getName());
            List<Long> ids = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            for (Image x : product.getImages()) {
                ids.add(x.getId());
                urls.add(x.getUrl());
            }
            addFields.setImageIds(ids);
            addFields.setUrls(urls);
            returnList.add(addFields);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_LISTED);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public DataResult<GetByIdProductResponse> getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdProductResponse getByIdProductResponse = mapperService.getModelMapper().map(product,
                GetByIdProductResponse.class);
        getByIdProductResponse.setCategoryId(product.getCategory().getId());
        getByIdProductResponse.setProducerId(product.getProducer().getId());
        getByIdProductResponse.setSupplierId(product.getSupplier().getId());
        List<Long> ids = new ArrayList<>();
        for (Image x : product.getImages()) {
            ids.add(x.getId());
        }
        getByIdProductResponse.setImageIds(ids);
        return new SuccessDataResult<>(getByIdProductResponse, GetByIdMessages.PRODUCT_LISTED);
    }

    @Override
    @Cacheable(value = "product")
    public DataResult<List<GetAllProductResponse>> getListBySorting(String sortBy) {
        isValidSortParameter(sortBy);

        List<GetAllProductResponse> returnList = new ArrayList<>();
        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        for (Product product : productList) {
            Product product1 = productRepository.findById(product.getId())
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            GetAllProductResponse addFields = mapperService.getModelMapper().map(product,
                    GetAllProductResponse.class);
            addFields.setCategoryId(product1.getCategory().getId());
            addFields.setProducerId(product1.getProducer().getId());
            addFields.setSupplierId(product1.getSupplier().getId());
            List<Long> ids = new ArrayList<>();
            for (Image x : product.getImages()) {
                ids.add(x.getId());
            }
            addFields.setImageIds(ids);
            returnList.add(addFields);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllProductResponse>> getListByPagination(int pageNo, int pageSize) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);

        List<GetAllProductResponse> returnList = new ArrayList<>();
        List<Product> productList = productRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        for (Product product : productList) {
            Product product1 = productRepository.findById(product.getId())
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            GetAllProductResponse addFields = mapperService.getModelMapper().map(product,
                    GetAllProductResponse.class);
            addFields.setCategoryId(product1.getCategory().getId());
            addFields.setProducerId(product1.getProducer().getId());
            addFields.setSupplierId(product1.getSupplier().getId());
            List<Long> ids = new ArrayList<>();
            for (Image x : product.getImages()) {
                ids.add(x.getId());
            }
            addFields.setImageIds(ids);
            returnList.add(addFields);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllProductResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);
        isValidSortParameter(sortBy);

        List<GetAllProductResponse> returnList = new ArrayList<>();
        List<Product> productList = productRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        for (Product product : productList) {
            Product product1 = productRepository.findById(product.getId())
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            GetAllProductResponse addFields = mapperService.getModelMapper().map(product,
                    GetAllProductResponse.class);
            addFields.setCategoryId(product1.getCategory().getId());
            addFields.setProducerId(product1.getProducer().getId());
            addFields.setSupplierId(product1.getSupplier().getId());
            List<Long> ids = new ArrayList<>();
            for (Image x : product.getImages()) {
                ids.add(x.getId());
            }
            addFields.setImageIds(ids);
            returnList.add(addFields);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_PAGINATED_AND_SORTED + sortBy);
    }

    @Override
    @Cacheable(value = "product", key = "#categoryId")
    public DataResult<List<GetAllProductResponse>> getAllByCategoryId(Long categoryId) {
        List<GetAllProductResponse> returnList = new ArrayList<>();
        List<Product> productList = productRepository.findAllByCategory_Id(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorMessages.CATEGORY_ID_NOT_FOUND));
        for (Product product : productList) {
            Product product1 = productRepository.findById(product.getId())
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            GetAllProductResponse addFields = mapperService.getModelMapper().map(product,
                    GetAllProductResponse.class);
            addFields.setCategoryId(product1.getCategory().getId());
            addFields.setCategoryName(product1.getCategory().getName());
            addFields.setProducerId(product1.getProducer().getId());
            addFields.setProducerName(product1.getProducer().getName());
            addFields.setSupplierId(product1.getSupplier().getId());
            addFields.setSupplierName(product1.getSupplier().getName());
            List<Long> ids = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            for (Image x : product.getImages()) {
                ids.add(x.getId());
                urls.add(x.getUrl());
            }
            addFields.setImageIds(ids);
            addFields.setUrls(urls);
            returnList.add(addFields);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_LISTED);
    }

    // bağımlılğı kontrol altına almak üzere tasarlandı
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.PRODUCT_ID_NOT_FOUND));
    }

    @Override
    public List<Product> getProductsByIds(Long[] productsId) {
        List<Product> resultList = new ArrayList<>();
        for (Long forEachId : productsId) {
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
            log.warn(LogWarnMessages.PRODUCT_NAME_REPEATED, name);
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
        Product checkField = new Product();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }

}
