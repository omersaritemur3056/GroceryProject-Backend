package com.example.grocery.business.concretes;

import com.example.grocery.business.abstracts.EmployeeService;
import com.example.grocery.business.abstracts.PhotoService;
import com.example.grocery.business.constants.Messages.*;
import com.example.grocery.business.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.business.constants.Messages.LogMessages.LogWarnMessages;
import com.example.grocery.core.security.services.UserService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.core.validation.mernisValidation.MernisValidationService;
import com.example.grocery.dataAccess.abstracts.EmployeeRepository;
import com.example.grocery.entity.concretes.Employee;
import com.example.grocery.entity.enums.Nationality;
import com.example.grocery.webApi.requests.employee.CreateEmployeeRequest;
import com.example.grocery.webApi.requests.employee.DeleteEmployeeRequest;
import com.example.grocery.webApi.requests.employee.UpdateEmployeeRequest;
import com.example.grocery.webApi.responses.employee.GetAllEmployeeResponse;
import com.example.grocery.webApi.responses.employee.GetByIdEmployeeResponse;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EmployeeManager implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MapperService mapperService;
    @Autowired
    private UserService userService;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private MernisValidationService mernisValidationService;

    @Override
    @Transactional
    public Result add(CreateEmployeeRequest createEmployeeRequest) {
        Result rules = BusinessRules.run(isExistNationalId(createEmployeeRequest.getNationalIdentity()),
                isPermissibleAge(createEmployeeRequest.getYearOfBirth()));
        if (!rules.isSuccess())
            return rules;

        Employee employee = mapperService.getModelMapper().map(createEmployeeRequest, Employee.class);
        employee.setUser(userService.getUserById(createEmployeeRequest.getUserId()));
        employee.setImage(photoService.getImageById(createEmployeeRequest.getImageId()));

        isTurkishCitizen(employee);

        employeeRepository.save(employee);
        log.info(LogInfoMessages.EMPLOYEE_ADDED, createEmployeeRequest.getFirstName(),
                createEmployeeRequest.getLastName());
        return new SuccessResult(CreateMessages.EMPLOYEE_CREATED);
    }

    @Override
    @Transactional
    public Result delete(DeleteEmployeeRequest deleteEmployeeRequest) {

        Result rules = BusinessRules.run(isExistId(deleteEmployeeRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Employee employee = mapperService.getModelMapper().map(deleteEmployeeRequest, Employee.class);
        Employee employeeForLog = employeeRepository.findById(deleteEmployeeRequest.getId())
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        log.info(LogInfoMessages.EMPLOYEE_DELETED, employeeForLog.getFirstName(),
                employeeForLog.getLastName());
        employeeRepository.delete(employee);
        return new SuccessResult(DeleteMessages.EMPLOYEE_DELETED);
    }

    @Override
    @Transactional
    public Result update(UpdateEmployeeRequest updateEmployeeRequest, Long id) {

        Result rules = BusinessRules.run(isExistNationalId(updateEmployeeRequest.getNationalIdentity()),
                isPermissibleAge(updateEmployeeRequest.getYearOfBirth()));
        if (!rules.isSuccess())
            return rules;

        Employee inDbEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Employee employee = mapperService.getModelMapper().map(updateEmployeeRequest, Employee.class);
        employee.setId(inDbEmployee.getId());
        employee.setUser(userService.getUserById(updateEmployeeRequest.getUserId()));
        employee.setImage(photoService.getImageById(updateEmployeeRequest.getImageId()));

        isTurkishCitizen(employee);

        employeeRepository.save(employee);
        log.info(LogInfoMessages.EMPLOYEE_UPDATED, updateEmployeeRequest.getFirstName(),
                updateEmployeeRequest.getLastName());
        return new SuccessResult(UpdateMessages.EMPLOYEE_MODIFIED);
    }

    @Override
    public DataResult<List<GetAllEmployeeResponse>> getAll() {
        List<Employee> employeeList = employeeRepository.findAll();
        List<GetAllEmployeeResponse> returnList = new ArrayList<>();
        for (Employee forEachEmployee : employeeList) {
            GetAllEmployeeResponse obj = mapperService.getModelMapper().map(forEachEmployee,
                    GetAllEmployeeResponse.class);
            obj.setUserId(forEachEmployee.getUser().getId());
            obj.setImageId(forEachEmployee.getImage().getId());
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.EMPLOYEES_LISTED);
    }

    @Override
    public DataResult<GetByIdEmployeeResponse> getById(Long id) {
        Employee inDbEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdEmployeeResponse returnObj = mapperService.getModelMapper().map(inDbEmployee,
                GetByIdEmployeeResponse.class);
        returnObj.setUserId(inDbEmployee.getUser().getId());
        returnObj.setImageId(inDbEmployee.getImage().getId());
        return new SuccessDataResult<>(returnObj, GetByIdMessages.EMPLOYEE_LISTED);
    }

    @Override
    public DataResult<List<GetAllEmployeeResponse>> getListBySorting(String sortBy) {
        isValidSortParameter(sortBy);

        List<Employee> employeeList = employeeRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllEmployeeResponse> returnList = new ArrayList<>();
        for (Employee forEachEmployee : employeeList) {
            GetAllEmployeeResponse obj = mapperService.getModelMapper().map(forEachEmployee,
                    GetAllEmployeeResponse.class);
            obj.setUserId(forEachEmployee.getUser().getId());
            obj.setImageId(forEachEmployee.getImage().getId());
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.EMPLOYEES_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllEmployeeResponse>> getListByPagination(int pageNo, int pageSize) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);

        List<Employee> employeeList = employeeRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllEmployeeResponse> returnList = new ArrayList<>();
        for (Employee forEachEmployee : employeeList) {
            GetAllEmployeeResponse obj = mapperService.getModelMapper().map(forEachEmployee,
                    GetAllEmployeeResponse.class);
            obj.setUserId(forEachEmployee.getUser().getId());
            obj.setImageId(forEachEmployee.getImage().getId());
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.EMPLOYEES_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllEmployeeResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);
        isValidSortParameter(sortBy);

        List<Employee> employeeList = employeeRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllEmployeeResponse> returnList = new ArrayList<>();
        for (Employee forEachEmployee : employeeList) {
            GetAllEmployeeResponse obj = mapperService.getModelMapper().map(forEachEmployee,
                    GetAllEmployeeResponse.class);
            obj.setUserId(forEachEmployee.getUser().getId());
            obj.setImageId(forEachEmployee.getImage().getId());
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.EMPLOYEES_PAGINATED_AND_SORTED + sortBy);
    }

    private Result isExistId(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistNationalId(String nationalId) {
        if (employeeRepository.existsByNationalIdentity(nationalId)) {
            log.warn(LogWarnMessages.NATIONAL_IDENTITY_REPEATED, nationalId);
            throw new BusinessException(ErrorMessages.NATIONAL_IDENTITY_REPEATED);
        }
        return new SuccessResult();
    }

    private Result isPermissibleAge(LocalDate birthYear) {
        if (LocalDate.now().getYear() - birthYear.getYear() < 18) {
            log.warn(LogWarnMessages.AGE_NOT_PERMISSIBLE, birthYear);
            throw new BusinessException(ErrorMessages.AGE_NOT_PERMISSIBLE);
        }
        return new SuccessResult();
    }

    private Result isTurkishCitizen(Employee employee) {
        if (mernisValidationService.validate(employee).isSuccess()) {
            employee.setNationality(Nationality.TURKISH);
            return new SuccessResult();
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
        Employee checkField = new Employee();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }

}
