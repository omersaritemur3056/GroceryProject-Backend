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
        return new SuccessResult("Product added.");
    }

    @Override
    public Result update(UpdateProductRequest updateProductRequest, int id) {

        Product inDbProduct = productRepository.findById(id).orElseThrow(() -> new BusinessException("Id not found!"));

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

        return new SuccessResult("Product modified.");
    }

    @Override
    public Result delete(DeleteProductRequest deleteProductRequest) {

        Result rules = BusinessRules.run(isExistId(deleteProductRequest.getId()));
        removeExpiratedProduct();

        Product product = mapperService.getModelMapper().map(deleteProductRequest, Product.class);

        Product productForLog = productRepository.findById(deleteProductRequest.getId())
                .orElseThrow(() -> new BusinessException("Id not found!"));
        log.info("removed product: {} logged to file!", productForLog.getName());
        productRepository.delete(product);

        return new SuccessResult("Product has been deleted.");
    }

    @Override
    public DataResult<List<GetAllProductResponse>> getAll() {
        List<GetAllProductResponse> returnList = new ArrayList<>();
        List<Product> productList = productRepository.findAll();
        for (Product product : productList) {
            Product product1 = productRepository.findById(product.getId()).get();
            GetAllProductResponse addFields = mapperService.getModelMapper().map(product,
                    GetAllProductResponse.class);
            addFields.setCategoryId(product1.getCategory().getId());
            addFields.setProducerId(product1.getProducer().getId());
            addFields.setSupplierId(product1.getSupplier().getId());
            returnList.add(addFields);
        }
        return new SuccessDataResult<List<GetAllProductResponse>>(returnList, "Products listed.");
    }

    @Override
    public DataResult<GetByIdProductResponse> getById(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new BusinessException("Id not found!"));
        GetByIdProductResponse getByIdProductResponse = mapperService.getModelMapper().map(product,
                GetByIdProductResponse.class);
        getByIdProductResponse.setCategoryId(product.getCategory().getId());
        getByIdProductResponse.setProducerId(product.getProducer().getId());
        getByIdProductResponse.setSupplierId(product.getSupplier().getId());
        return new SuccessDataResult<>(getByIdProductResponse, "Product listed");
    }

    // yorum bekle...
    private Product updateCategory(Product product, int id) {
        product.setCategory(categoryService.getCategoryById(id));
        return product;
    }

    private void removeExpiratedProduct() {
        for (Product product : productRepository.findAll()) {
            if (product.getExpirationDate().isBefore(LocalDate.now())) {
                productRepository.delete(product);
            }
        }
    }

    private Result isExistId(int id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException("Id not found!");
        }
        return new SuccessResult();
    }

    private Result isExistName(String name) {
        if (productRepository.existsByName(name)) {
            log.error("product name: {} couldn't saved", name);
            throw new BusinessException("Product name can not be repeated!");
        }
        return new SuccessResult();
    }

    private Result isExistCategoryId(int categoryId) {
        if (categoryService.getCategoryById(categoryId) == null) {
            throw new BusinessException("Category id not found!");
        }
        return new SuccessResult();
    }

    private Result isExistSupplierId(int supplierId) {
        if (supplierService.getSupplierById(supplierId) == null) {
            throw new BusinessException("Supplier id not found!");
        }
        return new SuccessResult();
    }

    private Result isExistProducerId(int producerId) {
        if (producerService.getProducerById(producerId) == null) {
            throw new BusinessException("Producer id not found!");
        }
        return new SuccessResult();
    }

}
