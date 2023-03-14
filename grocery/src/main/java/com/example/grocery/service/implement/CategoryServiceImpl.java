package com.example.grocery.service.concretes;

import com.example.grocery.service.abstracts.CategoryService;
import com.example.grocery.service.constants.Messages.*;
import com.example.grocery.service.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.service.rules.CategoryBusinessRules;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.CategoryRepository;
import com.example.grocery.entity.concretes.Category;
import com.example.grocery.api.requests.category.CreateCategoryRequest;
import com.example.grocery.api.requests.category.DeleteCategoryRequest;
import com.example.grocery.api.requests.category.UpdateCategoryRequest;
import com.example.grocery.api.responses.category.GetAllCategoryResponse;
import com.example.grocery.api.responses.category.GetByIdCategoryResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryManager implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MapperService mapperService;
    @Autowired
    private CategoryBusinessRules categoryBusinessRules;

    @Override
    public Result add(CreateCategoryRequest createCategoryRequest) {

        Result rules = BusinessRules.run(categoryBusinessRules.isExistName(createCategoryRequest.getName()));
        if (!rules.isSuccess())
            return rules;

        Category category = mapperService.forRequest().map(createCategoryRequest, Category.class);
        categoryRepository.save(category);
        log.info(LogInfoMessages.CATEGORY_ADDED, createCategoryRequest.getName());
        return new SuccessResult(CreateMessages.CATEGORY_CREATED);
    }

    @Override
    public Result delete(DeleteCategoryRequest deleteCategoryRequest) {

        Result rules = BusinessRules.run(categoryBusinessRules.isExistId(deleteCategoryRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Category category = mapperService.forRequest().map(deleteCategoryRequest, Category.class);
        log.info(LogInfoMessages.CATEGORY_DELETED, getCategoryById(deleteCategoryRequest.getId()).getName());
        categoryRepository.delete(category);
        return new SuccessResult(DeleteMessages.CATEGORY_DELETED);
    }

    @Override
    public Result update(UpdateCategoryRequest updateCategoryRequest, Long id) {

        Result rules = BusinessRules.run(categoryBusinessRules.isExistName(updateCategoryRequest.getName()),
                categoryBusinessRules.isExistId(id));
        if (!rules.isSuccess())
            return rules;

        Category inDbCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        Category category = mapperService.forRequest().map(updateCategoryRequest, Category.class);
        category.setId(inDbCategory.getId());
        categoryRepository.save(category);
        log.info(LogInfoMessages.CATEGORY_UPDATED, updateCategoryRequest.getName());
        return new SuccessResult(UpdateMessages.CATEGORY_MODIFIED);
    }

    @Override
    public DataResult<List<GetAllCategoryResponse>> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> mapperService.forResponse().map(c, GetAllCategoryResponse.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.CATEGORIES_LISTED);
    }

    @Override
    public DataResult<GetByIdCategoryResponse> getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdCategoryResponse getByIdCategoryResponse = mapperService.forResponse().map(category,
                GetByIdCategoryResponse.class);
        return new SuccessDataResult<>(getByIdCategoryResponse, GetByIdMessages.CATEGORY_LISTED);
    }

    @Override
    public DataResult<List<GetAllCategoryResponse>> getListBySorting(String sortBy) {
        categoryBusinessRules.isValidSortParameter(sortBy);

        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> mapperService.forResponse().map(c, GetAllCategoryResponse.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.CATEGORIES_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllCategoryResponse>> getListByPagination(int pageNo, int pageSize) {
        categoryBusinessRules.isPageNumberValid(pageNo);
        categoryBusinessRules.isPageSizeValid(pageSize);

        List<Category> categories = categoryRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> mapperService.forResponse().map(c, GetAllCategoryResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(returnList, GetListMessages.CATEGORIED_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllCategoryResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        categoryBusinessRules.isPageNumberValid(pageNo);
        categoryBusinessRules.isPageSizeValid(pageSize);
        categoryBusinessRules.isValidSortParameter(sortBy);

        List<Category> categories = categoryRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> mapperService.forResponse().map(c, GetAllCategoryResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(returnList, GetListMessages.CATEGORIES_PAGINATED_AND_SORTED + sortBy);
    }

    // ProductManager sınıfımızda bağımlılığı kontrol altına alma adına kullanılmak
    // üzere tasarlandı.
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.CATEGORY_ID_NOT_FOUND));
    }

}
