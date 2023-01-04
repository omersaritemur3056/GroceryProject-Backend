package com.example.grocery.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CategoryService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.modelMapper.ModelMapperService;
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
    private ModelMapperService modelMapperService;

    @Override
    public Result add(CreateCategoryRequest createCategoryRequest) {

        Result rules = BusinessRules.run(isExistName(createCategoryRequest.getName()));

        Category category = modelMapperService.getModelMapper().map(createCategoryRequest, Category.class);
        categoryRepository.save(category);
        log.info("succeed category : {} logged to file!", createCategoryRequest.getName());
        return new SuccessResult("Category saved on DB");
    }

    @Override
    public Result delete(DeleteCategoryRequest deleteCategoryRequest) {

        Result rules = BusinessRules.run(isExistId(deleteCategoryRequest.getId()));

        Category category = modelMapperService.getModelMapper().map(deleteCategoryRequest, Category.class);
        log.info("removed category: {} logged to file!", getId(deleteCategoryRequest.getId()).getName());
        categoryRepository.delete(category);
        return new SuccessResult("Category deleted on DB");
    }

    @Override
    public Result update(UpdateCategoryRequest updateCategoryRequest, int id) {

        Result rules = BusinessRules.run(isExistName(updateCategoryRequest.getName()), isExistId(id));

        var inDbCategory = categoryRepository.findById(id).orElseThrow(() -> new BusinessException("Id not found!"));
        Category category = modelMapperService.getModelMapper().map(updateCategoryRequest, Category.class);
        category.setId(inDbCategory.getId());
        categoryRepository.save(category);
        log.info("modified category : {} logged to file!", updateCategoryRequest.getName());
        return new SuccessResult("Category has been modified");
    }

    @Override
    public DataResult<List<GetAllCategoryResponse>> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<GetAllCategoryResponse> returnList = categories.stream()
                .map(c -> modelMapperService.getModelMapper().map(c, GetAllCategoryResponse.class)).toList();

        return new SuccessDataResult<>(returnList, "categories listed");
    }

    @Override
    public DataResult<GetByIdCategoryResponse> getById(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new BusinessException("Id not found!");
        }
        GetByIdCategoryResponse getByIdCategoryResponse = modelMapperService.getModelMapper().map(category,
                GetByIdCategoryResponse.class);
        return new SuccessDataResult<>(getByIdCategoryResponse, "Category is found by id");
    }

    // ProductManager sınıfımızda bağımlılığı kontrol altına alma adına kullanılmak
    // üzere tasarlandı.
    public Category getId(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new BusinessException("Category id not found!"));
    }

    private Result isExistId(int id) {
        if (!categoryRepository.existsById(id)) {
            log.error("Category id could not saved!", new BusinessException("Category id could not found!"));
            throw new BusinessException("Id was not found on DB!");
        }
        return new SuccessResult();
    }

    private Result isExistName(String name) {
        if (categoryRepository.existsByName(name)) {
            // update ve create için ayrı ve anlamlı bir log yaz.
            log.error("category name: {} couldn't saved", name);
            throw new BusinessException("Category name can not be repeated!");
        }
        return new SuccessResult();
    }

}
