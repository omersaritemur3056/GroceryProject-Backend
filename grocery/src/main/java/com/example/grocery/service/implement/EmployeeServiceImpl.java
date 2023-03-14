package com.example.grocery.service.concretes;

import com.example.grocery.service.abstracts.EmployeeService;
import com.example.grocery.service.abstracts.PhotoService;
import com.example.grocery.service.constants.Messages.*;
import com.example.grocery.service.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.service.rules.EmployeeBusinessRules;
import com.example.grocery.core.security.services.UserService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.EmployeeRepository;
import com.example.grocery.entity.concretes.Employee;
import com.example.grocery.api.requests.employee.CreateEmployeeRequest;
import com.example.grocery.api.requests.employee.DeleteEmployeeRequest;
import com.example.grocery.api.requests.employee.UpdateEmployeeRequest;
import com.example.grocery.api.responses.employee.GetAllEmployeeResponse;
import com.example.grocery.api.responses.employee.GetByIdEmployeeResponse;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private EmployeeBusinessRules employeeBusinessRules;

    @Override
    @Transactional
    public Result add(CreateEmployeeRequest createEmployeeRequest) {
        Result rules = BusinessRules.run(
                employeeBusinessRules.isExistNationalId(createEmployeeRequest.getNationalIdentity()),
                employeeBusinessRules.isPermissibleAge(createEmployeeRequest.getBirthYear()),
                employeeBusinessRules.isExistUserId(createEmployeeRequest.getUserId()),
                employeeBusinessRules.isExistImageId(createEmployeeRequest.getImageId()));
        if (!rules.isSuccess())
            return rules;

        Employee employee = mapperService.forRequest().map(createEmployeeRequest, Employee.class);
        employee.setUser(userService.getUserById(createEmployeeRequest.getUserId()));
        employee.setImage(photoService.getImageById(createEmployeeRequest.getImageId()));

        employeeBusinessRules.isTurkishCitizen(employee);

        employeeRepository.save(employee);
        log.info(LogInfoMessages.EMPLOYEE_ADDED, createEmployeeRequest.getFirstName(),
                createEmployeeRequest.getLastName());
        return new SuccessResult(CreateMessages.EMPLOYEE_CREATED);
    }

    @Override
    @Transactional
    public Result delete(DeleteEmployeeRequest deleteEmployeeRequest) {

        Result rules = BusinessRules.run(employeeBusinessRules.isExistId(deleteEmployeeRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Employee employee = mapperService.forRequest().map(deleteEmployeeRequest, Employee.class);
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
        Result rules = BusinessRules.run(employeeBusinessRules.isPermissibleAge(updateEmployeeRequest.getBirthYear()));
        if (!rules.isSuccess())
            return rules;

        Employee inDbEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Employee employee = mapperService.forRequest().map(updateEmployeeRequest, Employee.class);
        employee.setId(inDbEmployee.getId());
        employee.setUser(userService.getUserById(updateEmployeeRequest.getUserId()));
        employee.setImage(photoService.getImageById(updateEmployeeRequest.getImageId()));

        employeeBusinessRules.isTurkishCitizen(employee);

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
            GetAllEmployeeResponse obj = mapperService.forResponse().map(forEachEmployee,
                    GetAllEmployeeResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.EMPLOYEES_LISTED);
    }

    @Override
    public DataResult<GetByIdEmployeeResponse> getById(Long id) {
        Employee inDbEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdEmployeeResponse returnObj = mapperService.forResponse().map(inDbEmployee,
                GetByIdEmployeeResponse.class);
        return new SuccessDataResult<>(returnObj, GetByIdMessages.EMPLOYEE_LISTED);
    }

    @Override
    public DataResult<List<GetAllEmployeeResponse>> getListBySorting(String sortBy) {
        employeeBusinessRules.isValidSortParameter(sortBy);

        List<Employee> employeeList = employeeRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllEmployeeResponse> returnList = new ArrayList<>();
        for (Employee forEachEmployee : employeeList) {
            GetAllEmployeeResponse obj = mapperService.forResponse().map(forEachEmployee,
                    GetAllEmployeeResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.EMPLOYEES_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllEmployeeResponse>> getListByPagination(int pageNo, int pageSize) {
        employeeBusinessRules.isPageNumberValid(pageNo);
        employeeBusinessRules.isPageSizeValid(pageSize);

        List<Employee> employeeList = employeeRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllEmployeeResponse> returnList = new ArrayList<>();
        for (Employee forEachEmployee : employeeList) {
            GetAllEmployeeResponse obj = mapperService.forResponse().map(forEachEmployee,
                    GetAllEmployeeResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.EMPLOYEES_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllEmployeeResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        employeeBusinessRules.isPageNumberValid(pageNo);
        employeeBusinessRules.isPageSizeValid(pageSize);
        employeeBusinessRules.isValidSortParameter(sortBy);

        List<Employee> employeeList = employeeRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllEmployeeResponse> returnList = new ArrayList<>();
        for (Employee forEachEmployee : employeeList) {
            GetAllEmployeeResponse obj = mapperService.forResponse().map(forEachEmployee,
                    GetAllEmployeeResponse.class);
            returnList.add(obj);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.EMPLOYEES_PAGINATED_AND_SORTED + sortBy);
    }

}
