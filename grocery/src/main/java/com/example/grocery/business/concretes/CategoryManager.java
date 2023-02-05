package com.example.grocery.business.concretes;

import com.example.grocery.business.abstracts.CategoryService;
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
import com.example.grocery.dataAccess.abstracts.CategoryRepository;
import com.example.grocery.entity.concretes.Category;
import com.example.grocery.webApi.requests.category.CreateCategoryRequest;
import com.example.grocery.webApi.requests.category.DeleteCategoryRequest;
import com.example.grocery.webApi.requests.category.UpdateCategoryRequest;
import com.example.grocery.webApi.responses.category.GetAllCategoryResponse;
import com.example.grocery.webApi.responses.category.GetByIdCategoryResponse;

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

    @Override
    public Result add(CreateCategoryRequest createCategoryRequest) {

        Result rules = BusinessRules.run(isExistName(createCategoryRequest.getName()));
        if (!rules.isSuccess())
            return rules;

        Category category = mapperService.getModelMapper().map(createCategoryRequest, Category.class);
        categoryRepository.save(category);
        log.info(LogInfoMessages.CATEGORY_ADDED, createCategoryRequest.getName());
        return new SuccessResult(CreateMessages.CATEGORY_CREATED);
    }

    @Override
    public Result delete(DeleteCategoryRequest deleteCategoryRequest) {

        Result rules = BusinessRules.run(isExistId(deleteCategoryRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Category category = mapperService.getModelMapper().map(deleteCategoryRequest, Category.class);
        log.info(LogInfoMessages.CATEGORY_DELETED, getCategoryById(deleteCategoryRequest.getId()).getName());
        categoryRepository.delete(category);
        return new SuccessResult(DeleteMessages.CATEGORY_DELETED);
    }

    @Override
    public Result update(UpdateCategoryRequest updateCategoryRequest, Long id) {

        Result rules = BusinessRules.run(isExistName(updateCategoryRequest.getName()), isExistId(id));
        if (!rules.isSuccess())
            return rules;

        Category inDbCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        Category category = mapperService.getModelMapper().map(updateCategoryRequest, Category.class);
        category.setId(inDbCategory.getId());
        categoryRepository.save(category);
        log.info(LogInfoMessages.CATEGORY_UPDATED, updateCategoryRequest.getName());
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

    @Override
    public DataResult<List<GetAllCategoryResponse>> getListBySorting(String sortBy) {
        isValidSortParameter(sortBy);

        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> mapperService.getModelMapper().map(c, GetAllCategoryResponse.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.CATEGORIES_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllCategoryResponse>> getListByPagination(int pageNo, int pageSize) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);

        List<Category> categories = categoryRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> mapperService.getModelMapper().map(c, GetAllCategoryResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(returnList, GetListMessages.CATEGORIED_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllCategoryResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);
        isValidSortParameter(sortBy);

        List<Category> categories = categoryRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> mapperService.getModelMapper().map(c, GetAllCategoryResponse.class))
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

    private Result isExistId(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistName(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            // update ve create için ayrı ve anlamlı bir log yaz.
            log.warn(LogWarnMessages.CATEGORY_NAME_REPEATED, name);
            throw new BusinessException(ErrorMessages.CATEGORY_NAME_REPEATED);
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
        Category checkField = new Category();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }

}
