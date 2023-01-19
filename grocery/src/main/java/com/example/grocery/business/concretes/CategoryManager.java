package com.example.grocery.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CategoryService;
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
import com.example.grocery.dataAccess.abstracts.CategoryRepository;
import com.example.grocery.entity.concretes.Category;
import com.example.grocery.webApi.requests.category.CreateCategoryRequest;
import com.example.grocery.webApi.requests.category.DeleteCategoryRequest;
import com.example.grocery.webApi.requests.category.UpdateCategoryRequest;
import com.example.grocery.webApi.responses.category.GetAllCategoryResponse;
import com.example.grocery.webApi.responses.category.GetByIdCategoryResponse;

import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryManager implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MapperService mapperService;

    @Override
    public Result add(CreateCategoryRequest createCategoryRequest) {

        Result rules = BusinessRules.run(isExistName(createCategoryRequest.getName()));

        Category category = mapperService.getModelMapper().map(createCategoryRequest, Category.class);
        categoryRepository.save(category);
        log.info("succeed category : {} logged to file!", createCategoryRequest.getName());
        return new SuccessResult(CreateMessages.CATEGORY_CREATED);
    }

    @Override
    public Result delete(DeleteCategoryRequest deleteCategoryRequest) {

        Result rules = BusinessRules.run(isExistId(deleteCategoryRequest.getId()));

        Category category = mapperService.getModelMapper().map(deleteCategoryRequest, Category.class);
        log.info("removed category: {} logged to file!", getCategoryById(deleteCategoryRequest.getId()).getName());
        categoryRepository.delete(category);
        return new SuccessResult(DeleteMessages.CATEGORY_DELETED);
    }

    @Override
    public Result update(UpdateCategoryRequest updateCategoryRequest, Long id) {

        Result rules = BusinessRules.run(isExistName(updateCategoryRequest.getName()), isExistId(id));

        var inDbCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        Category category = mapperService.getModelMapper().map(updateCategoryRequest, Category.class);
        category.setId(inDbCategory.getId());
        categoryRepository.save(category);
        log.info("modified category : {} logged to file!", updateCategoryRequest.getName());
        return new SuccessResult(UpdateMessages.CATEGORY_MODIFIED);
    }

    @Override
    public DataResult<List<GetAllCategoryResponse>> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> mapperService.getModelMapper().map(c, GetAllCategoryResponse.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.CATEGORIES_LISTED);
    }

    @Override
    public DataResult<GetByIdCategoryResponse> getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdCategoryResponse getByIdCategoryResponse = mapperService.getModelMapper().map(category,
                GetByIdCategoryResponse.class);
        return new SuccessDataResult<>(getByIdCategoryResponse, GetByIdMessages.CATEGORY_LISTED);
    }

    // ProductManager sınıfımızda bağımlılığı kontrol altına alma adına kullanılmak
    // üzere tasarlandı.
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.CATEGORY_ID_NOT_FOUND));
    }

    private Result isExistId(Long id) {
        if (!categoryRepository.existsById(id)) {
            log.warn("Category id could not found!");
            throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistName(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            // update ve create için ayrı ve anlamlı bir log yaz.
            log.warn("category name: {} couldn't saved", name);
            throw new BusinessException(ErrorMessages.CATEGORY_NAME_REPEATED);
        }
        return new SuccessResult();
    }

}
