package com.example.grocery.business.concretes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.EmployeeService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.UpdateMessages;
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
import com.example.grocery.webApi.requests.employee.CreateEmployeeRequest;
import com.example.grocery.webApi.requests.employee.DeleteEmployeeRequest;
import com.example.grocery.webApi.requests.employee.UpdateEmployeeRequest;
import com.example.grocery.webApi.responses.employee.GetAllEmployeeResponse;
import com.example.grocery.webApi.responses.employee.GetByIdEmployeeResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeManager implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MapperService mapperService;
    @Autowired
    private UserService userService;

    @Override
    public Result add(CreateEmployeeRequest createEmployeeRequest) {
        // asgari ücretin altında salary olamaz eklenebilir...
        Result rules = BusinessRules.run(isExistNationalId(createEmployeeRequest.getNationalIdentity()),
                isPermissibleAge(createEmployeeRequest.getYearOfBirth()));

        Employee employee = mapperService.getModelMapper().map(createEmployeeRequest, Employee.class);
        employee.setUser(userService.getUserById(createEmployeeRequest.getUserId()));
        employeeRepository.save(employee);
        log.info("added employee: {} {} logged to file!", createEmployeeRequest.getFirstName(),
                createEmployeeRequest.getLastName());
        return new SuccessResult(CreateMessages.EMPLOYEE_CREATED);
    }

    @Override
    public Result delete(DeleteEmployeeRequest deleteEmployeeRequest) {

        Result rules = BusinessRules.run(isExistId(deleteEmployeeRequest.getId()));

        Employee employee = mapperService.getModelMapper().map(deleteEmployeeRequest, Employee.class);
        Employee employeeForLog = employeeRepository.findById(deleteEmployeeRequest.getId())
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        log.info("deleted employee: {} {} logged to file!", employeeForLog.getFirstName(),
                employeeForLog.getLastName());
        employeeRepository.delete(employee);
        return new SuccessResult(DeleteMessages.EMPLOYEE_DELETED);
    }

    @Override
    public Result update(UpdateEmployeeRequest updateEmployeeRequest, Long id) {

        Result rules = BusinessRules.run(isExistNationalId(updateEmployeeRequest.getNationalIdentity()),
                isPermissibleAge(updateEmployeeRequest.getYearOfBirth()));

        Employee inDbEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Employee employee = mapperService.getModelMapper().map(updateEmployeeRequest, Employee.class);
        employee.setId(inDbEmployee.getId());
        employee.setUser(userService.getUserById(updateEmployeeRequest.getUserId()));
        employeeRepository.save(employee);
        log.info("modified employee: {} {} logged to file!", updateEmployeeRequest.getFirstName(),
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
        return new SuccessDataResult<>(returnObj, GetByIdMessages.EMPLOYEE_LISTED);
    }

    private Result isExistId(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistNationalId(String nationalId) {
        if (employeeRepository.existsByNationalIdentity(nationalId)) {
            throw new BusinessException(ErrorMessages.NATIONAL_IDENTITY_REPEATED);
        }
        return new SuccessResult();
    }

    private Result isPermissibleAge(LocalDate birthYear) {
        if (LocalDate.now().getYear() - birthYear.getYear() < 18) {
            throw new BusinessException(ErrorMessages.AGE_NOT_PERMISSIBLE);
        }
        return new SuccessResult();
    }

}
