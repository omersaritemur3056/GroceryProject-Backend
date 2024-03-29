package com.example.grocery.service.implement;

import com.example.grocery.service.rules.ProductBusinessRules;
import com.example.grocery.service.interfaces.*;
import com.example.grocery.service.constants.Messages.*;
import com.example.grocery.service.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.repository.ProductRepository;
import com.example.grocery.model.concretes.Product;
import com.example.grocery.api.requests.product.CreateProductRequest;
import com.example.grocery.api.requests.product.DeleteProductRequest;
import com.example.grocery.api.requests.product.UpdateProductRequest;
import com.example.grocery.api.responses.product.GetAllProductResponse;
import com.example.grocery.api.responses.product.GetByIdProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProducerService producerService;
    private final SupplierService supplierService;
    private final MapperService mapperService;
    private final PhotoService photoService;
    private final ProductBusinessRules productBusinessRules;

    @Override
    @Transactional
    @CacheEvict(cacheNames = "product", allEntries = true)
    public Result add(CreateProductRequest createProductRequest) {
        Result rules = BusinessRules.run(productBusinessRules.isExistName(createProductRequest.getName()),
                productBusinessRules.isExistCategoryId(createProductRequest.getCategoryId()));
        if (!rules.isSuccess())
            return rules;

        Product addProduct = mapperService.forRequest().map(createProductRequest, Product.class);
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

        Result rules = BusinessRules.run(productBusinessRules.isExistName(updateProductRequest.getName()),
                productBusinessRules.isExistId(id),
                productBusinessRules.isExistCategoryId(updateProductRequest.getCategoryId()),
                productBusinessRules.isExistProducerId(updateProductRequest.getProducerId()),
                productBusinessRules.isExistSupplierId(updateProductRequest.getSupplierId()));
        if (!rules.isSuccess())
            return rules;

        Product product = mapperService.forRequest().map(updateProductRequest, Product.class);
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
        Result rules = BusinessRules.run(productBusinessRules.isExistId(deleteProductRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Product product = mapperService.forRequest().map(deleteProductRequest, Product.class);

        Product productForLog = productRepository.findById(deleteProductRequest.getId())
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        log.info(LogInfoMessages.PRODUCT_DELETED, productForLog.getName());
        productRepository.delete(product);
        return new SuccessResult(DeleteMessages.PRODUCT_DELETED);
    }

    @Override
    @Cacheable(value = "product")
    public DataResult<List<GetAllProductResponse>> getAll() {
        productBusinessRules.findExpiredProduct();

        List<Product> productList = productRepository.findAll();
        List<GetAllProductResponse> returnList = productList.stream()
                .map(p -> {
                    GetAllProductResponse returnObj = mapperService.forResponse().map(p, GetAllProductResponse.class);
                    returnObj.setImageIds(p.getImages().stream().map(image -> image.getId()).toList());
                    returnObj.setUrls(p.getImages().stream().map(url -> url.getUrl()).toList());
                    return returnObj;
                })
                .toList();
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_LISTED);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public DataResult<GetByIdProductResponse> getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdProductResponse getByIdProductResponse = mapperService.forResponse().map(product,
                GetByIdProductResponse.class);
        getByIdProductResponse.setImageIds(product.getImages().stream().map(image -> image.getId()).toList());
        getByIdProductResponse.setUrls(product.getImages().stream().map(url -> url.getUrl()).toList());
        return new SuccessDataResult<>(getByIdProductResponse, GetByIdMessages.PRODUCT_LISTED);
    }

    @Override
    @Cacheable(value = "product")
    public DataResult<List<GetAllProductResponse>> getListBySorting(String sortBy) {
        productBusinessRules.findExpiredProduct();

        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllProductResponse> returnList = productList.stream()
                .map(p -> {
                    GetAllProductResponse returnObj = mapperService.forResponse().map(p, GetAllProductResponse.class);
                    returnObj.setImageIds(p.getImages().stream().map(image -> image.getId()).toList());
                    returnObj.setUrls(p.getImages().stream().map(url -> url.getUrl()).toList());
                    return returnObj;
                })
                .toList();
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllProductResponse>> getListByPagination(int pageNo, int pageSize) {
        productBusinessRules.isPageNumberValid(pageNo);
        productBusinessRules.isPageSizeValid(pageSize);
        productBusinessRules.findExpiredProduct();

        List<Product> productList = productRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllProductResponse> returnList = productList.stream()
                .map(p -> {
                    GetAllProductResponse returnObj = mapperService.forResponse().map(p, GetAllProductResponse.class);
                    returnObj.setImageIds(p.getImages().stream().map(image -> image.getId()).toList());
                    returnObj.setUrls(p.getImages().stream().map(url -> url.getUrl()).toList());
                    return returnObj;
                })
                .toList();
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllProductResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        productBusinessRules.isPageNumberValid(pageNo);
        productBusinessRules.isPageSizeValid(pageSize);
        productBusinessRules.findExpiredProduct();

        List<Product> productList = productRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllProductResponse> returnList = productList.stream()
                .map(p -> {
                    GetAllProductResponse returnObj = mapperService.forResponse().map(p, GetAllProductResponse.class);
                    returnObj.setImageIds(p.getImages().stream().map(image -> image.getId()).toList());
                    returnObj.setUrls(p.getImages().stream().map(url -> url.getUrl()).toList());
                    return returnObj;
                })
                .toList();
        return new SuccessDataResult<>(returnList, GetListMessages.PRODUCTS_PAGINATED_AND_SORTED + sortBy);
    }

    @Override
    @Cacheable(value = "product", key = "#categoryId")
    public DataResult<List<GetAllProductResponse>> getAllByCategoryId(Long categoryId) {
        List<Product> productList = productRepository.findAllByCategory_Id(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorMessages.CATEGORY_ID_NOT_FOUND));
        List<GetAllProductResponse> returnList = productList.stream()
                .map(p -> {
                    GetAllProductResponse returnObj = mapperService.forResponse().map(p, GetAllProductResponse.class);
                    returnObj.setImageIds(p.getImages().stream().map(image -> image.getId()).toList());
                    returnObj.setUrls(p.getImages().stream().map(url -> url.getUrl()).toList());
                    return returnObj;
                })
                .toList();
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
        if (productsId.length < 1) {
            throw new BusinessException("Insert product IDs!");
        }
        List<Long> x = Arrays.stream(productsId).toList();
        List<Product> resultList = productRepository.findAllById(x);
        return resultList;
    }
}
